package dchq.dbconnect.config;

import com.mongodb.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @since 12/9/2015
 */
@Configuration
public class MongoConfig {
    public static final String MONGO_URL = "mongo_url";

    private static final Pattern PATTERN = Pattern.compile("^([^:]+):(\\d+)/(\\w+)$");
    private static final Logger log = LoggerFactory.getLogger(MongoConfig.class);

    @Autowired
    private Environment env;

    private String host;
    private int port;
    private String database;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        if (!this.isMongoUrlProvided()) {
            return null;
        }

        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoDbFactory mongoDbFactory = this.mongoDbFactory();
        if (mongoDbFactory == null) {
            return null;
        }

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);

        return mongoTemplate;
    }

    protected boolean parseConnectionString(String text) {
        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            host = matcher.group(1);
            port = Integer.parseInt(matcher.group(2));
            database = matcher.group(3);

            return true;
        } else {
            log.error("Can't parse mongo_url: " + text);
            return false;
        }
    }

    public boolean isMongoUrlProvided() {
        String mongoUrl = env.getProperty(MONGO_URL);
        if (mongoUrl == null || !this.parseConnectionString(mongoUrl)) {
            return false;
        }

        return true;
    }
}
