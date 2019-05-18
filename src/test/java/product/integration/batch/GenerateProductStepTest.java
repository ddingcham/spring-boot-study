package product.integration.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.StepRunner;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = {GenerateProductStepTest.TempStepDefinition.class})
@TestPropertySource(properties = "rawProductPath=/test-dataset/test-product/set")
@RunWith(SpringJUnit4ClassRunner.class)
public class GenerateProductStepTest implements ApplicationContextAware {

    private static final int NUM_OF_TEST_PRODUCTS = 300;

    private ApplicationContext context;

    private StepRunner stepRunner;

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

    }
}
