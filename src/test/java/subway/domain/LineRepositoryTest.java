package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @AfterEach
    void after() {
        stations.deleteAllInBatch();
        lines.deleteAllInBatch();
    }

    @Test
    void saveWithLine() {
        final Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(station);
        stations.flush();
    }

    @Test
    void findByNameWithLine() {
        stations.save(new Station("교대역"));
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("3호선")));

        assertThat(station).isNotNull();
        assertThat(station.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        stations.save(new Station("교대역"));
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    void findByName() {
        lines.save(new Line("3호선"));
        Line line = lines.findByName("3호선");
        Station station = stations.save(new Station("경복궁역"));
        station.setLine(line);
        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    void save() {
        Line line = new Line("2호선");
        line.addStation(new Station("잠실역"));
        Line save = lines.save(line);
    }

}