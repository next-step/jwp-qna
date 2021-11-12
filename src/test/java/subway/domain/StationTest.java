package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationTest {
	@Autowired
	private StationRepository stations;
	
	@Autowired
	private LineStationRepository lineStations;

	@Test
	void name() {
		//ddl 확인용
	}

	@Test
	void save() {
		final Station station = new Station("잠실역");
		final Station actual = stations.save(station);
		assertThat(actual.getId()).isNotNull();

		assertThat(actual.getName()).isEqualTo("잠실역");
	}

	@Test
	void findByName() {
		Station station1 = stations.save(new Station("잠실역"));
		Station station2 = stations.findByName("잠실역");
		assertThat(station2.getId()).isEqualTo(station1.getId());
		assertThat(station2.getName()).isEqualTo(station1.getName());
		assertThat(station2).isEqualTo(station1);
		assertThat(station2).isSameAs(station1);
	}

	@Test
	void update() {
		final Station station1 = stations.save(new Station("잠실역"));
		station1.changeName("몽촌토성역");
		Station station2 = stations.findByName("몽촌토성역");
		assertThat(station2).isNotNull();
	}

	@Test
	void saveWithLineStation() {
		LineStation lineStation = lineStations.save(new LineStation());
		stations.save(new Station("잠실역", lineStation));
	}
}
