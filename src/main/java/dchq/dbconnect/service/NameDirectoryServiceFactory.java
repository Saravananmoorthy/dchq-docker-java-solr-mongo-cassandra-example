package dchq.dbconnect.service;

import dchq.dbconnect.config.CassandraConfig;
import dchq.dbconnect.config.DatabaseConfig;
import dchq.dbconnect.config.MongoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @since 11/25/2015
 */
public class NameDirectoryServiceFactory {
    private static NameDirectoryServiceFactory service = new NameDirectoryServiceFactory();

    @Autowired
    private Environment env;

    public NameDirectoryService createService() {
        String driverClassName = env.getProperty(DatabaseConfig.DRIVER_CLASS_NAME);
        if (driverClassName != null) {
            if (driverClassName.contains("mysql")) {
                return new NameDirectoryServiceMysql();
            } else if (driverClassName.contains("postgresql")) {
                return new NameDirectoryServicePostgresql();
            } else if (driverClassName.contains("oracle")) {
                return new NameDirectoryServiceOracle();
            }
        }

        String mongoUrl = env.getProperty(MongoConfig.MONGO_URL);
        if (mongoUrl != null) {
            return new NameDirectoryServiceMongo();
        }

        if (env.getProperty(CassandraConfig.CASSANDRA_URL) != null) {
            return new NameDirectoryServiceCassandra();
        }

        throw new UnsupportedOperationException("Unknown Database Type");
    }

    public static NameDirectoryServiceFactory createFactory() {
        return service;
    }
}
