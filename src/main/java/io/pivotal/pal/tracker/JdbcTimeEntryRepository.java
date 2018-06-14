package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<TimeEntry> mapper = (row, ind) -> new TimeEntry(
            row.getLong("id"),
            row.getLong("project_id"),
            row.getLong("user_id"),
            row.getDate("date").toLocalDate(),
            row.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry toCreate) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) VALUES " +
                    "(?, ?, ?, ?)", RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, toCreate.getProjectId());
            preparedStatement.setLong(2, toCreate.getUserId());
            preparedStatement.setDate(3, Date.valueOf(toCreate.getDate()));
            preparedStatement.setInt(4, toCreate.getHours());

            return preparedStatement;
        }, generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(Long id) {
        return jdbcTemplate.query(
                "SELECT * FROM time_entries WHERE id = ?",
                new Object[]{id},
                extractor
        );
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "SELECT * FROM time_entries",
                mapper
        );
    }

    @Override
    public TimeEntry update(Long id, TimeEntry updated) {
        jdbcTemplate.update(
                "UPDATE time_entries SET project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ?",
                updated.getProjectId(),
                updated.getUserId(),
                Date.valueOf(updated.getDate()),
                updated.getHours(),
                id
        );

        return find(id);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }
}
