package product.integration.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import product.BulkApplication;

@TestPropertySource(properties = {
        "rawProductPath=/test-dataset/test-product-set",
        "spring.batch.job.name=bulkProduct",
        "logging.level.org.springframework.batch.core: DEBUG",
        "logging.level.org.springframework.data.mongodb.core.MongoTemplate: DEBUG"})
@SpringBootTest(classes = BulkApplication.class)
@RunWith(SpringRunner.class)
public class BulkProductJobLauncher {

    @Test
    public void launchEndToEnd() {
        // for monitoring log
    }
}
