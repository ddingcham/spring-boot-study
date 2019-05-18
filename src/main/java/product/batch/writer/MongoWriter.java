package product.batch.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import product.domain.Product;
import product.domain.ProductRepository;

@Slf4j
@Configuration
@EnableMongoRepositories(basePackages = "product.domain")
public class MongoWriter {
    @Bean
    public ItemWriter<Product> rawProductWriter(ProductRepository productRepository) {
        return productRepository::saveAll;
    }
}