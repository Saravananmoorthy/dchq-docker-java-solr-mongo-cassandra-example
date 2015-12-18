package dchq.dbconnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

/**
 * @since 12/9/2015
 */
@Configuration
public class SolrConfig {
    public static final String SOLR_HOST = "solr_host";
    public static final String SOLR_PORT = "solr_port";

    @Autowired
    private Environment env;

    private String solrHost;
    private int solrPort;

    @Bean
    public HttpSolrServerFactoryBean solrServerFactoryBean() {
        HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

        factory.setUrl(this.getSolrUrl());

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        solrHost = env.getProperty(SOLR_HOST);
        solrPort = Integer.parseInt(env.getProperty(SOLR_PORT, "8983"));

        if (solrHost == null) {
            return null;
        }

        return new SolrTemplate(solrServerFactoryBean().getObject());
    }

    protected String getSolrUrl() {
        return String.format("http://%s:%d/solr/names/", solrHost, solrPort);
    }

}
