package learningtest.subway;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class SubwayJpaTest {

	@Autowired
	StationRepository stations;
	@Autowired
	LinesRepository lines;

	@Test
	void merge_cascade() {
		// line = transient(new)
		Line line = new Line("line1");
		// station = detached
		Station station = new Station(1L, "name", line);
		System.out.println("### before merge");
		// mergedStation
		Station mergedStation = stations.save(station);
		System.out.println("### after merge");

		assertThat(station).isNotEqualTo(mergedStation);
		assertThat(station.getLine())
			.extracting(Line::getId)
			.isNull();
		assertThat(mergedStation.getLine()).isNotEqualTo(line);
		assertThat(mergedStation.getLine())
			.extracting(Line::getName).isNotNull();
	}
}
