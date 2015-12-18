package dchq.dbconnect.model;

import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @since 11/22/2015
 */
@SolrDocument(solrCoreName = "names")
public class NameDirectory {
    public NameDirectory() {
    }

    public NameDirectory(NameDirectory from) {
        this.id = from.id;
        this.firstName = from.firstName;
        this.lastName = from.lastName;
        this.createdTimestamp = from.createdTimestamp;
    }

    @Indexed
    private Long id;

    @Indexed
    private String firstName;

    @Indexed
    private String lastName;

    private Date createdTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
