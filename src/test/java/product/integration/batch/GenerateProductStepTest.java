package product.integration.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.test.StepRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import product.domain.Product;
import product.domain.support.ProductNo;
import product.domain.support.ProductOrigin;
import product.domain.support.ProductProperties;
import product.integration.batch.support.IdGenerator;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = {GenerateProductStepTest.TempStepDefinition.class})
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
    static class TempStepDefinition {

        private static final int DEFAULT_CHUNK_SIZE = 10;

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
