package study.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StationRepositoryTest {
	@Autowired
	private StationRepository stations;
	@Autowired
	private LineRepository lines;
	@Autowired
	private FavoriteRepository favorites;
	@Autowired
	private MemberRepository members;
	@Autowired
	private LineStationRepository lineStations;

	//1.7.3. 다대일 단방향 연관 관계 - findByNameWithLine()
	@BeforeEach
	void initialize() {
		Line line3 = lines.save(new Line("3호선"));
		Station station = new Station("교대역");
		station.setLine(line3);
		stations.save(station);
		line3.addStation(station);
		lines.flush();
		stations.flush();
	}

	@DisplayName("1.4. Spring Data JPA - save()")
	@Test
	@Order(1)
	void save() {
		Station expected = new Station("잠실역");
		Station actual = stations.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getName()).isEqualTo(expected.getName())
		);
	}

	@DisplayName("1.4. Spring Data JPA - findByName()")
	@Test
	@Order(2)
	void findByName() {
		String expected = "잠실역";
		stations.save(new Station(expected));
		String actual = stations.findByName(expected).getName();
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("1.5. 영속성 컨텍스트 - 동일성 보장 맛보기")
	@Test
	@Order(3)
	void identity() {
		Station station1 = stations.save(new Station("잠실역"));
		Station station2 = stations.findById(station1.getId()).get();
		assertThat(station1 == station2).isTrue();
	}

	@DisplayName("1.5. 영속성 컨텍스트 - 변경 감지 맛보기")
	@Test
	@Order(4)
	void update() {
		Station station1 = stations.save(new Station("잠실역"));
		station1.changeName("몽촌토성역");
		Station station2 = stations.findByName("몽촌토성역");
		assertThat(station2).isNotNull();
	}

	/**
	 * JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다.
	 * - new Line("2호선") 이 영속 상태가 아니므로 영속상태로 만들어야 Exception이 발생하지 않는다.
	 */
	@DisplayName("1.7.3. 다대일 단방향 연관 관계 - 저장 : saveWithLine() - Exception 발생")
	@Test
	@Order(5)
	void saveWithLineThrownByException() {
		Station expected = new Station("잠실역");
		expected.setLine(new Line("2호선"));
		Station actual = stations.save(expected);
		assertThatThrownBy(() -> stations.flush()).isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

	@DisplayName("1.7.3. 다대일 단방향 연관 관계 - 저장 : saveWithLine()")
	@Test
	@Order(6)
	void saveWithLine() {
		Station expected = new Station("잠실역");
		expected.setLine(lines.save(new Line("2호선")));
		Station actual = stations.save(expected);
		stations.flush(); // transaction commit
	}

	/**
	 * data.sql 에 INSERT Query가 들어있어서 조회가 가능하다.
	 */
	@DisplayName("1.7.3. 다대일 단방향 연관 관계 - 조회 : findByNameWithLine()")
	@Test
	@Order(7)
	void findByNameWithLine() {
		Station actual = stations.findByName("교대역");
		assertThat(actual).isNotNull();
		assertThat(actual.getLine().getName()).isEqualTo("3호선");
	}

	@DisplayName("1.7.3. 다대일 단방향 연관 관계 - 수정 : updateWithLine()")
	@Test
	@Order(8)
	void updateWithLine() {
		Station expected = stations.findByName("교대역");
		expected.setLine(lines.save(new Line("2호선")));
		stations.flush(); // transaction commit
	}

	@DisplayName("1.7.3. 다대일 단방향 연관 관계 - 연관 관계 제거 : removeLine()")
	@Test
	@Order(9)
	void removeLine() {
		Station expected = stations.findByName("교대역");
		expected.setLine(null);
		stations.flush(); // transaction commit
		/*
		 * **주의사항**
		 * 노선을 삭제하려면 기존에 있던 연관 관계를 먼저 제거하고 삭제해야 한다.
		 */
	}

	@DisplayName("1.7.4. 양방향 연관 관계 - 조회 : findById() [Line]")
	@Test
	@Order(10)
	void findById() {
		Line line = lines.findByName("3호선");
		assertThat(line.getStations()).hasSize(1);
	}

	/**
	 * 연관 관계의 주인이 아닌 곳에 입력된 값은 외래 키에 영향을 주지 않는다.
	 * -> respository를 통하여 영속 상태(stations.save(new Station("역이름"))를 만들든,
	 *    비영속 상태(new Station("역이름"))이든 관계 없이 영향을 주지 않는다.
	 */
	@DisplayName("1.7.4.2. 연관 관계의 주인 - 연관 관계 없는 저장 : saveLineWithoutSaveStation()")
	@Test
	@Order(11)
	void saveLineWithoutSaveStation() {
		Line expected = new Line("2호선");
		expected.addStation(new Station("잠실역"));
		lines.save(expected);
		lines.flush(); // transaction commit
	}

	/**
	 * 연관 관계의 주인이 아닌 곳에 입력된 값은 외래 키에 영향을 주지 않는다.
	 * -> respository를 통하여 영속 상태(stations.save(new Station("역이름"))를 만들든,
	 *    비영속 상태(new Station("역이름"))이든 관계 없이 영향을 주지 않는다.
	 */
	@DisplayName("1.7.4.2. 연관 관계의 주인 - 연관 관계 없는 저장 : saveLineWithSaveStation()")
	@Test
	@Order(12)
	void saveLineWithSaveStation() {
		Line expected = new Line("2호선");
		expected.addStation(stations.save(new Station("잠실역")));
		lines.save(expected);
		lines.flush(); // transaction commit
	}

	/**
	 * - 양방향 연관 관계는 결국 양쪽 다 신경 써야 한다.
	 *    station.setLine(line);
	 *    line.addStation(station);
	 * - 양방향 관계에서 두 코드는 하나인 것처럼 사용하는 것이 안전하다.
	 * - 한 번에 양방향 관계를 설정하는 메서드를 연관 관계 편의 메서드 라 한다.
	 *
	 * 아래는 station에 연관관계가 line6만 되어 있음에도
	 * line5의 연관이 남아있는 현상을 보여준다.
	 */
	@DisplayName("1.7.4.4~5. 연관 관계 편의 메서드 - 작성 시 주의 사항 : cautionInterRelation()")
	@Test
	@Order(13)
	void cautionInterRelation() {
		Line line5 = lines.save(new Line("5호선"));
		Line line6 = lines.save(new Line("6호선"));
		Station station = stations.save(new Station("공덕역"));

		station.setLineInteraction(line5);
		station.setLineInteraction(line6);
		assertThat(line5.getStations().contains(station)).isTrue(); // true
	}

	/**
	 * - 매핑한 객체가 관리하는 외래 키가 다른 테이블에 있다.
	 * - 연관 관계 처리를 위한 UPDATE SQL을 추가로 실행해야 한다.
	 * - 일대다 단방향 매핑보다는 다대일 양방향 매핑을 권장한다.
	 */
	@DisplayName("1.7.5. 일대다 단방향 연관 관계 - 저장 : saveOneToMany()")
	@Test
	@Order(14)
	void saveOneToMany() {
		Member expected = new Member("jason");
		expected.addFavorite(favorites.save(new Favorite()));
		Member actual = members.save(expected);
		members.flush(); // transaction commit
	}

	@DisplayName("1.7.6.1.1.2. 일대일 연관 관계 - 주 테이블에 외래 키 - 단방향 연관 관계 - 저장 : saveWithLineStation()")
	@Test
	@Order(15)
	void saveWithLineStation() {
		LineStation lineStation = lineStations.save(new LineStation());
		stations.save(new Station("잠실역", lineStation));
	}
}
