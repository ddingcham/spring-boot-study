package product.common.config.database;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "product.domain.book",
//        repositoryBaseClass = EditableRepository.class,
        entityManagerFactoryRef = "editableEntityManagerFactory",
        transactionManagerRef = "transactionManagerForEditableEntity",
        includeFilters = @Filter(EditableRepository.class)
)
@RequiredArgsConstructor
@Slf4j
public class EditableDatabaseConfig {

    private static final String ENTITY_PACKAGE_PATH_TO_SCAN = "product.domain.book";

    @Primary
    @Bean(name = "editableEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean editableEntityManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("editableDataSource") DataSource dataSource) {
        //            TODO : more EntityManager Properties
        return builder
                .dataSource(dataSource)
                .packages(ENTITY_PACKAGE_PATH_TO_SCAN)
                .build();
    }

    @Primary
    @Bean(name = "editableDataSource")
    @ConfigurationProperties(prefix = "editable.datasource")
    public DataSource editableDataSource() {
        //  TODO           ex) Connection Pool config - HikariCP
        return DataSourceBuilder
                .create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "transactionManagerForEditableEntity")
    public PlatformTransactionManager transactionManagerForEditableEntity(@Qualifier("editableEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
