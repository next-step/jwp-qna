package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @BeforeEach
    void setup() {
        Station station = stations.save(new Station("교대역"));
        Line line = lines.save(new Line("3호선"));
        station.setLine(line);
        line.addStation(station);
    }

    @Test
    void saveWithLine() {
        Station station1 = new Station("잠실역");
        station1.setLine(lines.save(new Line("2호선")));
        Station save = stations.save(station1);
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
        line.addStation(stations.save(new Station("잠실역")));
        lines.save(line);
        lines.flush();
    }
}
