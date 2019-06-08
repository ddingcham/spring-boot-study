package product.common.config.database;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "product.domain.store",
        repositoryBaseClass = ReadOnlyRepository.class,
        entityManagerFactoryRef = "readOnlyEntityManager"
)
// TODO : Validate TransactionManager Config when readonly
@RequiredArgsConstructor
public class ReadOnlyDatabaseConfig {

    private static final String ENTITY_PACKAGE_PATH_TO_SCAN_STORE = "product.domain.store";

    @Bean
    public LocalContainerEntityManagerFactoryBean readOnlyEntitiyManager(EntityManagerFactoryBuilder builder,
                                                                         @Qualifier("readOnlyDataSource") DataSource dataSource) {
//            TODO : more EntityManager Properties
        return builder
                .dataSource(dataSource)
                .packages(ENTITY_PACKAGE_PATH_TO_SCAN_STORE)
                .build();
    }

    @Bean(name = "readOnlyDataSource")
    @ConfigurationProperties(prefix = "readonly.datasource")
    public DataSource readOnlyDataSource() {
        //  TODO           ex) Connection Pool config - HikariCP
        return DataSourceBuilder
                .create()
                .build();
    }
}
