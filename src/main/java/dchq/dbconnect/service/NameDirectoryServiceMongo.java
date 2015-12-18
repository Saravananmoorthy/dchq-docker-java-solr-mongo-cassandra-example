package dchq.dbconnect.service;

import dchq.dbconnect.model.NameDirectory;
import dchq.dbconnect.model.NameDirectoryNoSQL;
import dchq.dbconnect.model.SequenceId;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 12/9/2015
 */
public class NameDirectoryServiceMongo implements NameDirectoryService {
    private static final String COLLECTION_NAME = "names";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<NameDirectory> getAllRows() {
        List<NameDirectory> result = new ArrayList<NameDirectory>();
        result.addAll(mongoTemplate.findAll(NameDirectoryNoSQL.class, COLLECTION_NAME));
        return result;
    }

    @Override
    public NameDirectory getById(Long id) {
        List<NameDirectoryNoSQL> list = mongoTemplate.find(new BasicQuery("{id: " + id + "}"), NameDirectoryNoSQL.class, COLLECTION_NAME);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return new NameDirectory(list.iterator().next());
    }

    @Override
    public Long addNameDirectory(NameDirectory nd) {
        NameDirectoryNoSQL mongo = new NameDirectoryNoSQL(nd);

        Long id = this.getNextSequence(COLLECTION_NAME);
        Date createdTimestamp = new Date();
        mongo.setId(id);
        mongo.setCreatedTimestamp(createdTimestamp);

        mongoTemplate.save(mongo, COLLECTION_NAME);

        nd.setId(id);
        nd.setCreatedTimestamp(createdTimestamp);

        return id;
    }

    @Override
    public void deleteNameDirectoryById(Long id) {
        mongoTemplate.remove(new BasicQuery("{id: " + id + "}"), COLLECTION_NAME);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), COLLECTION_NAME);
    }

    private Long getNextSequence(String name) {
        Query query = new Query(Criteria.where("_id").is(name));

        Update update = new Update();
        update.inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
        if (seqId == null) {
            seqId = new SequenceId();
            seqId.setId(name);
            mongoTemplate.save(seqId);
        }

        return seqId.getSeq();
    }
}
