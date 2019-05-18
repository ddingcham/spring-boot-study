package product.integration.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.StepRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import product.domain.Product;
import product.integration.batch.support.IdGenerator;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = {GenerateProductStepTest.TempStepDefinition.class})
@TestPropertySource(properties = "rawProductPath=/test-dataset/test-product/set")
@RunWith(SpringJUnit4ClassRunner.class)
public class GenerateProductStepTest implements ApplicationContextAware {

    private static final int NUM_OF_TEST_PRODUCTS = 300;

    private ApplicationContext context;

    private StepRunner stepRunner;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private Map<BigInteger, Product> mockStore;

    @Before
    public void setUp() {
        stepRunner = new StepRunner(jobLauncher, jobRepository);
    }

    @Test
    public void temp_validate_fixture() {
        assertThat(stepRunner).isNotNull();
        assertThat(idGenerator).isNotNull();
        assertThat(mockStore).isNotNull();
    }

    @Test
    public void validate_num_of_products() {
        Step step = (Step) context.getBean("generateProducts");
        assertThat(stepRunner.launchStep(step).getStatus())
                .isEqualTo(BatchStatus.COMPLETED);
        assertThat(mockStore)
                .hasSize(NUM_OF_TEST_PRODUCTS);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;

    }

    @Configuration
    @EnableBatchProcessing
    static class TempStepDefinition {

        // Mock Mongo(Key-Value Document Store)
        @Bean
        public Map<BigInteger, Product> mockStore() {
            return new ConcurrentHashMap<>();
        }

        @Bean
        public IdGenerator idGenerator() {
            return new MockIdGenerator();
        }

    }

    static class MockIdGenerator implements IdGenerator {

        private long INITIAL_VALUE = 1L;
        private AtomicLong current;

        public MockIdGenerator() {
            current = new AtomicLong(INITIAL_VALUE);
        }

        @Override
        public long next() {
            return current.getAndIncrement();
        }

        @Override
        public void reset() {
            current.set(INITIAL_VALUE);
        }
    }
}
