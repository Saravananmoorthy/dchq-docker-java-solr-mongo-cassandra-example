package dchq.dbconnect.service;

import dchq.dbconnect.model.NameDirectory;
import dchq.dbconnect.model.NameDirectoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @since 11/25/2015
 */
public class NameDirectoryServicePostgresql implements NameDirectoryService {
    private static final Logger log = LoggerFactory.getLogger(NameDirectoryServicePostgresql.class);

    @Autowired
    private DataSource dataSource;

    private final NameDirectoryMapper mapper = new NameDirectoryMapper();

    @Override
    public List<NameDirectory> getAllRows() {
        return getJdbcTemplate().query("SELECT * FROM \"NameDirectory\"", new Object[]{}, mapper);
    }

    @Override
    public NameDirectory getById(Long id) {
        return getJdbcTemplate().queryForObject("SELECT * FROM NameDirectory WHERE id = ?", new Object[]{id}, mapper);
    }

    @Override
    public Long addNameDirectory(final NameDirectory nd) {
        final String sql = "INSERT INTO \"NameDirectory\" (\"firstName\", \"lastName\") VALUES (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(sql, new String[]{"id"});
                        ps.setString(1, nd.getFirstName());
                        ps.setString(2, nd.getLastName());
                        return ps;
                    }
                }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteNameDirectoryById(Long id) {
        getJdbcTemplate().update("DELETE FROM \"NameDirectory\" WHERE id = ?",
                new Object[]{id});
    }

    @Override
    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM \"NameDirectory\"",
                new Object[]{});
    }

    protected JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
