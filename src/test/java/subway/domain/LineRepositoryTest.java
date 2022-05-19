package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @BeforeEach
    void setup(){
        final Line saveLine = lines.save(new Line("3호선"));
        final Station station = stations.save(new Station("교대역"));
        station.setLine(saveLine);
    }


    @Test
    void saveWithLine() {
        final Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(station);
        stations.flush();

    }

    @Test
    void findByNameWithLine() {
        final Station station = stations.findByName("교대역");
        assertThat(station).isNotNull();
        assertThat(station.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    void findByName() {
        final Line line  = lines.findByName("3호선");
        assertThat(line.getStation()).hasSize(1);
    }

    @Test
    void save() {
        final Line line = new Line("2호선");
        line.addStation(stations.save(new Station("잠실역")));
        lines.save(line);
        lines.flush();
    }


}