package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    void saveWithLine() {
        Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(station);
        stations.flush();
    }

    @Test
    void findByNameWithLine() {
        Station station = stations.findByName("교대역");
        assertThat(station).isNotNull();
        assertThat(station.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    void removeLine() {
        Station station = stations.findByName("교대역");
        station.setLine(null);
        stations.flush();
    }

    @Test
    void findByName() {
        Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
        assertThat(line.getStations().get(0).getName()).isEqualTo("교대역");
    }

    @Test
    void save() {
        Line line = new Line("2호선");
        line.addStation(new Station("잠실역"));
        lines.save(line);
        lines.flush();
    }
}