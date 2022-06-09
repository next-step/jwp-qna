package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @DisplayName("엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다")
    @Test
    void saveWithLine() {
        Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        stations.save(station);
        stations.flush();
    }

    @DisplayName("객체는 자유롭게 객체 그래프를 탐색할 수 있어야 한다")
    @Test
    void findByNameWithLine() {
        Station station = stations.findByName("교대역");
        assertThat(station).isNotNull();
        assertThat(station.getLine().getName()).isEqualTo("3호선");
    }

    @DisplayName("한 트랜잭션 안에서 발생하는 엔티티의 변경사항을 감지한다")
    @Test
    void updateWithLine() {
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @DisplayName("NULL을 주입하면 연관된 엔티티를 삭제할 수 있다")
    @Test
    void removeLine() {
        Station station = stations.findByName("교대역");
        station.setLine(null);
        stations.flush();
    }

    @DisplayName("객체를 양방향으로 참조하려면 단방향 연관 관계를 2개 만들어야 한다")
    @Test
    void findByName() {
        Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
    }

    @DisplayName("연관 관계의 주인만이 외래 키를 등록, 수정, 삭제할 수 있다")
    @Test
    void save() {
        Line line = new Line("2호선");
        line.addStation(stations.save(new Station("잠실역")));
        lines.save(line);
        lines.flush();
    }
}
