package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationTest {
	@Autowired
	private StationRepository stations;

	@Test
	@DisplayName("저장")
	public void saveTest() {
		final Station station = new Station("잠실역");
		final Station actual = stations.save(station);
		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo("잠실역");
	}

	@Test
	@DisplayName("findByID 테스트")
	public void StationTest() {
		final Station station1 = stations.save(new Station("강남역"));
		final Station station2 = stations.findById(station1.getId()).get();
		assertThat(station1).isSameAs(station2);
	}

	@Test
	@DisplayName("update 테스트")
	public void updateTest() {
		final Station station1 = stations.save(new Station("천안역"));
		station1.changeName("몽촌토성역");
		final Station station2 = stations.findByName("몽촌토성역");
		assertThat(station2).isNotNull();
	}

}

