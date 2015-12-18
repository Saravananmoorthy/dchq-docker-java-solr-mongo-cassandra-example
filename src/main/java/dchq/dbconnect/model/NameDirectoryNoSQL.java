package dchq.dbconnect.model;

import com.datastax.driver.core.DataType;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.mapping.CassandraType;
import org.springframework.data.cassandra.mapping.Table;

/**
 * @since 12/10/2015
 */
@Table
public class NameDirectoryNoSQL extends NameDirectory {
    public NameDirectoryNoSQL() {
    }

    public NameDirectoryNoSQL(NameDirectory from) {
        super(from);
    }

    @Id
    private String objectId;

    @Override
    @CassandraType(type = DataType.Name.INT)
    public Long getId() {
        return super.getId();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
