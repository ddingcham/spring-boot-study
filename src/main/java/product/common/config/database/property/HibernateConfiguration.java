package product.common.config.database.property;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class HibernateConfiguration {
    static String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    static String HIBERNATE_DIALECT = "hibernate.dialect";
    static String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";


    private final Environment env;

    @Bean(name = "hibernateProperties")
    public Map<String, Object> hibernateProperties() {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put(HIBERNATE_HBM2DDL_AUTO, env.getProperty(HIBERNATE_HBM2DDL_AUTO));
        properties.put(HIBERNATE_DIALECT, env.getProperty(HIBERNATE_DIALECT));
        properties.put(HIBERNATE_CONNECTION_URL, env.getProperty(HIBERNATE_CONNECTION_URL));
        return properties;
    }
}
