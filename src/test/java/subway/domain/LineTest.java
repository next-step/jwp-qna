package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineTest {

	@Autowired
	LineRepository lines;

	@Autowired
	StationRepository stations;

	@Test
	void saveWithLine() {
		final Station expected = new Station("잠실역");
		// Station에 연관관계를 맺고 있는 Line이 영속 상태가 아니어서 Error 발생
		// JSP에서 Entity를 저장할 때 연관된 모든 Entity는 영속 상태여야 함.
		// expected.setLine(new Line("2호선")); --> 그래서 Error 발생
		expected.setLine(lines.save(new Line("2호선")));
		final Station actual = stations.save(expected);
		stations.flush();   // commit
	}

	@Test
	void findByNameWithLine() {
		final Station actual = stations.findByName("교대역");
		assertThat(actual).isNotNull();
		assertThat(actual.getLine().getName()).isEqualTo("3호선");
	}

	@Test
	void updateWithLine() {
		final Station station = stations.findByName("교대역");
		station.setLine(lines.save(new Line("2호선")));
		stations.flush();
	}
	
	@Test
	void removeLine() {
		final Station station = stations.findByName("교대역");
		station.setLine(null);  // Line과의 연관관계를 끊어주기 위해 null 입력
		// 연관관계가 끊어질 뿐 3호선이 지워지는 것은 아님.
		// 노선을 삭제하고 싶으면 모든 연관관계를 끊은 후 삭제할 수 있음.
		stations.flush();
	}

	@Test
	void findById() {
		final Line line = lines.findByName("3호선");
		assertThat(line.getStations()).hasSize(1);
	}

	@Test
	void save() {
		final Line expected = new Line("2호선");
		expected.addStation(stations.save(new Station("잠실역")));
		lines.save(expected);
		lines.flush();
	}
}
