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
import org.springframework.batch.item.ItemWriter;
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
import product.batch.step.GenerateProductStep;
import product.domain.Product;
import product.integration.batch.support.IdGenerator;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = {GenerateProductStepTest.TestConfiguration.class, GenerateProductStep.class})
@TestPropertySource(properties = "rawProductPath=/test-dataset/test-product-set")
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
        mockStore.clear();
        idGenerator.reset();
        stepRunner = new StepRunner(jobLauncher, jobRepository);
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
    static class TestConfiguration {
        // Mock Mongo(Key-Value Document Store)
        @Bean
        public Map<BigInteger, Product> mockStore() {
            return new ConcurrentHashMap<>();
        }

        @Bean
        public IdGenerator idGenerator() {
            return new MockIdGenerator();
        }

        @Bean
        public ItemWriter<Product> mockProductWriter(Map<BigInteger, Product> mockStore, IdGenerator idGenerator) {
            return items -> mockStore.putAll(items
                    .stream()
                    .peek((product) -> product.setId(BigInteger.valueOf(idGenerator.next())))
                    .collect(Collectors.toMap(Product::getId, Function.identity())));
        }

    }

    static class MockIdGenerator implements IdGenerator {

        private long INITIAL_VALUE = 0L;
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
