package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
public class LineRepositoryTest {

    @Autowired
    private LineRepository lines;


    @Autowired
    private StationRepository stations;

    Station STATION;
    Line LINE;

    @BeforeEach
    public void setUp() throws Exception {
        STATION = new Station("교대역");
        LINE = lines.save(new Line("3호선"));
        STATION.setLine(LINE);
        stations.save(STATION);
    }

    @Test
    void saveWithLine() {
        Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(expected);
        stations.flush();
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");

        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void removeLine() {
        Station station = stations.findByName("교대역");
        station.removeLine();
        stations.flush();
    }

    @Test
    void findById() {
        Line line = lines.findByName("3호선");
        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
        lines.flush();
    }
}
