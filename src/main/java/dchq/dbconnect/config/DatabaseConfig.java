package dchq.dbconnect.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import snaq.db.DBPoolDataSource;

/**
 * @since 12/10/2015
 */
@Configuration
public class DatabaseConfig {

    public static final String DRIVER_CLASS_NAME = "database_driverClassName";
    public static final String DATABASE_URL = "database_url";
    public static final String DATABASE_USERNAME = "database_username";
    public static final String DATABASE_PASSWORD = "database_password";

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "release")
    public DBPoolDataSource dataSource() {
        if (!isDatabaseProvided()) {
            return null;
        }
        DBPoolDataSource dbPoolDataSource = new DBPoolDataSource();
        dbPoolDataSource.setDriverClassName(env.getRequiredProperty(DRIVER_CLASS_NAME));
        dbPoolDataSource.setUrl(env.getRequiredProperty(DATABASE_URL));
        dbPoolDataSource.setUser(env.getRequiredProperty(DATABASE_USERNAME));
        dbPoolDataSource.setPassword(env.getRequiredProperty(DATABASE_PASSWORD));
        dbPoolDataSource.setMinPool(1);
        dbPoolDataSource.setMaxPool(10);
        dbPoolDataSource.setMaxSize(10);
        dbPoolDataSource.setIdleTimeout(60);

        return dbPoolDataSource;
    }

    @Bean
    public SpringLiquibase liquibase() {
        if (!isDatabaseProvided()) {
            return null;
        }
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setChangeLog("/WEB-INF/upgrade/upgrade.sql");
        springLiquibase.setDataSource(this.dataSource());

        return springLiquibase;
    }

    public boolean isDatabaseProvided() {
        return env.getProperty(DRIVER_CLASS_NAME) != null;
    }
}
