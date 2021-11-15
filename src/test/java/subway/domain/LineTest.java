package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineTest {

	@Autowired
	private LineRepository lines;

	@Autowired
	private StationRepository stations;

	@Test
	public void LineTest() {
		final Station expected = new Station("천안역");
		expected.setLine(lines.save(new Line("2호선")));
		final Station actual = stations.save(expected);
		stations.flush();
	}

	@Test
	void save() {
		final Line expected = new Line("2호선");
		expected.addStation(stations.save(new Station("잠실역")));
		lines.save(expected);
		lines.flush();

	}
}
