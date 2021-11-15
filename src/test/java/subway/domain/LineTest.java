package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @BeforeEach
    public void setUp() {
        Line line = new Line("3호선");
        Station station = new Station("교대역");
        lines.save(line);
        stations.save(station);
    }

    @Test
    public void saveWithLine() {
        Station expected = new Station("잠실역");
        Line line = new Line("2호선");
        expected.setLine(line);
        line.addStation(expected);
        lines.save(line);
        Station actual = stations.save(expected);
        stations.flush(); // commit
    }

    @Test
    public void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    public void updateWithLine() {
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    public void removeLine() {
        Station station = stations.findByName("교대역");
        station.setLine(null);
        stations.flush();
    }

    @Test
    public void findById() {
        Line line = lines.findByName("3호선");
        Assertions.assertThat(line.getStations()).hasSize(1);
    }

    @Test
    public void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
    }
}
