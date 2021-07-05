package subway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineRepositoryTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    public void saveWithLine() {
        final Station expected = new Station("신림역");
        expected.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(expected);
        stations.flush();
    }

    @Test
    public void findByNameWithLine() {
        final Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    public void updateWithLine() {
        final Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush();
//        assertThat(actual).isNotNull();
//        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void removeLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(null);
        stations.flush();
    }

    @Test
    void save() {
        final Line expected = new Line("2호선");
//        expected.addStation(new Station("신림역"));
        expected.addStation(stations.save(new Station("신림역")));
        assertThat(expected.getStations()).hasSize(1);
        lines.save(expected);
        lines.flush();
    }
}
