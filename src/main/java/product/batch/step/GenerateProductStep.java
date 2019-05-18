package product.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import product.domain.Product;
import product.domain.support.ProductNo;
import product.domain.support.ProductOrigin;
import product.domain.support.ProductProperties;

@Configuration
@EnableBatchProcessing
public class GenerateProductStep {

    private static final int DEFAULT_CHUNK_SIZE = 30;

    @Bean
    public Step generateProducts(StepBuilderFactory stepBuilderFactory, ItemReader<Product> rawProductReader, ItemWriter<Product> rawProductWriter) {
        return stepBuilderFactory
                .get("generateProducts")
                .<Product, Product>chunk(DEFAULT_CHUNK_SIZE)
                .reader(rawProductReader)
                .writer(rawProductWriter)
                .build();
    }

    @Bean
    public ItemReader<Product> rawProductReader(@Value("${rawProductPath}") Resource rawProducts, LineMapper<Product> rawProductLineMapper) {
        FlatFileItemReader<Product> rawProductReader = new FlatFileItemReader<>();

        rawProductReader.setResource(rawProducts);
        rawProductReader.setLineMapper(rawProductLineMapper);

        return rawProductReader;
    }

    @Bean
    public LineMapper<Product> rawProductLineMapper(LineTokenizer rawProductTokenizer, FieldSetMapper<Product> rawProductEntityMapper) {
        DefaultLineMapper<Product> productLineMapper = new DefaultLineMapper<>();

        productLineMapper.setLineTokenizer(rawProductTokenizer);
        productLineMapper.setFieldSetMapper(rawProductEntityMapper);

        return productLineMapper;
    }

    @Bean
    public LineTokenizer rawProductTokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
        tokenizer.setNames("productNo", "origin", "recommendScore");
        tokenizer.setIncludedFields(0, 1, 2);
        return tokenizer;
    }

    @Bean
    public FieldSetMapper<Product> rawProductEntityMapper() {
        return fieldSet -> {
            ProductOrigin origin = ProductOrigin.of(fieldSet.readString("origin"));
            return Product.builder()
                    .productNo(new ProductNo(fieldSet.readLong("productNo")))
                    .origin(origin)
                    .productProperties(ProductProperties
                            .builder(origin)
                            .build())
                    .recommendScore(fieldSet.readInt("recommendScore"))
                    .build();
        };
    }
}
