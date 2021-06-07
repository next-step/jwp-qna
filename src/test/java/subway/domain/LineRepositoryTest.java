package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    public void saveWithLine() {
        Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(expected);
        stations.flush();
    }

    @Test
    public void findByNameWithLine() {
        saveWithLine();
        Line line = lines.findById(1L).get();
        Station actual = stations.findByName("잠실역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("2호선");
    }

    @Test
    public void updateWithLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    public void removeLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(null);
        stations.flush();
    }

    @Test
    public void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save((new Station("잠실역"))));
        assertThat(expected.getStations()).hasSize(1);
        lines.save(expected);
        lines.flush();
    }

}