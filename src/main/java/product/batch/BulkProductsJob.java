package product.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "product.batch.step")
@Configuration
@EnableBatchProcessing
public class BulkProductsJob {

    @Bean
    public Job bulkProduct(JobBuilderFactory jobBuilderFactory, Step generateProducts) {
        return jobBuilderFactory
                .get("bulkProduct")
                .incrementer(new RunIdIncrementer())
                .start(generateProducts)
                .build();
    }

}