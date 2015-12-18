package dchq.dbconnect.service;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import dchq.dbconnect.model.NameDirectory;
import dchq.dbconnect.model.NameDirectoryNoSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @since 12/12/2015
 */
public class NameDirectoryServiceCassandra implements NameDirectoryService {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    private AtomicLong nextId = new AtomicLong(1);

    @Override
    public List<NameDirectory> getAllRows() {
        List<NameDirectory> result = new ArrayList<NameDirectory>();
        result.addAll(cassandraTemplate.selectAll(NameDirectoryNoSQL.class));

        for (NameDirectory nameDirectory : result) {
            while (nameDirectory.getId() > nextId.longValue()) {
                nextId.compareAndSet(nextId.longValue(), nameDirectory.getId());
            }
        }

        return result;
    }

    @Override
    public NameDirectory getById(Long id) {
        Select select = QueryBuilder.select().from(NameDirectoryNoSQL.class.getSimpleName());
        select.where(QueryBuilder.eq("id", id));
        NameDirectoryNoSQL from = cassandraTemplate.selectOne(select, NameDirectoryNoSQL.class);
        return new NameDirectory(from);
    }

    @Override
    public Long addNameDirectory(NameDirectory nd) {
        this.getAllRows();

        NameDirectoryNoSQL item = new NameDirectoryNoSQL(nd);

        Long id = this.getNextSequence();
        Date createdTimestamp = new Date();

        item.setId(id);
        item.setObjectId(UUID.randomUUID().toString());
        item.setCreatedTimestamp(createdTimestamp);

        nd.setId(id);
        nd.setCreatedTimestamp(createdTimestamp);

        cassandraTemplate.insert(item);

        return id;
    }

    @Override
    public void deleteNameDirectoryById(Long id) {
        NameDirectory item = this.getById(id);
        cassandraTemplate.delete(item);
    }

    @Override
    public void deleteAll() {
        cassandraTemplate.deleteAll(NameDirectoryNoSQL.class);
    }

    private Long getNextSequence() {
        return nextId.incrementAndGet();
    }
}
