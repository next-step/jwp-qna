package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {
	@Autowired
	private LineRepository lines;
	
	@Autowired
	private StationRepository stations;

	@BeforeEach
	void setUp() {
		Station expected = new Station("교대역");
		expected.setLine(lines.save(new Line("3호선")));
		stations.save(expected);
	}

	@Test
	void saveWithLine() {
		Station expected = new Station("잠실역");
		expected.setLine(lines.save(new Line("2호선"))); // 연관객체 또한 영속화 되어 있어야 한다.

		Station actual = stations.save(expected);
		assertSame(expected, actual);
	}

	@Test
	void findByNameWithLine() {
		Station actual = stations.findByName("교대역");
		assertEquals(actual.getLine().getName(), "3호선");
	}

	@Test
	void updateWithLine() {
		Station expected = stations.findByName("교대역");
		expected.setLine(lines.save(new Line("2호선")));
		Station actual = stations.findByName("교대역");

		assertEquals(actual.getLine().getName(), "2호선");
	}

	@Test
	void remove() {
		Station expected = stations.findByName("교대역");
		expected.setLine(null);
		Station actual = stations.findByName("교대역");

		assertNull(actual.getLine());
	}

	@Test
	void save() {
		Line expected = new Line("2호선");
		expected.addStation(stations.save(new Station("잠실역")));
		lines.save(expected);
		lines.flush();
	}
}
