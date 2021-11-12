package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @DisplayName("station 저장 with line")
    @Test
    void saveWithLine() {
        final Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(expected);
        stations.flush();
    }

    @DisplayName("station 에서 line 찾기")
    @Test
    void findByNameWithLine() {
        final Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        // 객체 간 탐색이 자유로움 -> 지연 로딩
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @DisplayName("station 에서 line update")
    @Test
    void updateWithLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @DisplayName("station 에서 line remove")
    @Test
    void removeWithLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(null);
        stations.flush();
    }

    @DisplayName("line id로 조회")
    @Test
    void findById() {
        final Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
    }

    @DisplayName("line station 추가 후 저장")
    @Test
    void save() {
        final Line expected = new Line("2호선");
        // station 쪽이 외래키 관리자이기 때문에 station 안쪽에서 편의 메서드로 만들어 주어야 함.
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
        lines.flush();
    }
}
