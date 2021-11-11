package subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

@DataJpaTest
public class LineTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    void saveWithLine() {
        final Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(expected);
        stations.flush();
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
        station.setLine(null);
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
