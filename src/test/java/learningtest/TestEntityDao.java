package learningtest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestEntityDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public TestEntity findById(Long id) {
		return jdbcTemplate.queryForObject(
			"select * from test_entity where id = " + id,
			(rs, rowNum) -> new TestEntity(rs.getLong(1), rs.getString(2)));
	}

	public int getCount() {
		return jdbcTemplate.queryForObject(
			"select count(*) from test_entity",
			(rs, rowNum) -> rs.getInt(1));
	}

	public List<TestEntity> findAll() {
		return jdbcTemplate.query("select * from test_entity",
			(rs, rowNum) -> new TestEntity(rs.getLong(1), rs.getString(2)));
	}

	public void deleteAll() {
		jdbcTemplate.execute("truncate table test_entity");
	}
}
