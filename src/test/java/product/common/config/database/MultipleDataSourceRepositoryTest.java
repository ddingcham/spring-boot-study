package product.common.config.database;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import product.domain.book.BookRepository;
import product.domain.store.StoreRepository;


@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = {EditableDatabaseConfig.class, ReadOnlyDatabaseConfig.class})
@TestPropertySource("/application-datasource.yml")
@DataJpaTest
public class MultipleDataSourceRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void contextLoads() {
    }
}


/*
    TODO
    When @DataJpaTest
    Parameter 0 of method testEntityManager in org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration required a single bean, but 2 were found:
    For Test Configuration - singular

 */