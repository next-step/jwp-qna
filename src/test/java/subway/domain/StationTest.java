package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JPA 관련 테스트
 *  - @DataJpaTest에 Transaction Annotation 존재
 */
@DataJpaTest
public class StationTest {

	@Autowired
	private StationRepository stations;

	@Test
	void save() {
		final Station station = new Station("잠실역");
		final Station actual = stations.save(station);
		assertThat(actual.getId()).isNotNull();
		assertThat(actual.getName()).isEqualTo("잠실역");
	}

	@Test
	void findByName() {
		final Station station1 = stations.save(new Station("잠실역"));
		final Station station2 = stations.findByName("잠실역");
		assertThat(station2.getId()).isEqualTo(station1.getId());
		assertThat(station2.getName()).isEqualTo(station1.getName());
		// 영속성 컨텍스트로 인해 동일성 보장
		assertThat(station2).isEqualTo(station1);
		assertThat(station2).isSameAs(station1);
	}

	@Test
	void findById() {
		final Station station1 = stations.save(new Station("잠실역"));
		final Station station2 = stations.findById(station1.getId()).get();
		assertThat(station2).isSameAs(station1);
	}

	@Test
	void update() {
		final Station station1 = stations.save(new Station("잠실역"));
		// Console에 출력된 내용을 보면 Insert 후 Update하는 것을 확인할 수 있음.
		station1.changeName("몽촌토성역");
		// Snapshot으로 인해 변경이 안된 것으로 판단하고 Update 쿼리 실행 안함
		// station1.changeName("잠실역");
		final Station station2 = stations.findByName("몽촌토성역");
		assertThat(station2).isNotNull();
	}
}
