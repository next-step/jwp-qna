package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineRepositoryTest {
    @Autowired
    private LineRepository lines;
    
    @Autowired
    private StationRepository stations;

    @Test
    void findById() {
        final Line line = lines.findByName("3호선");
        assertThat(line.getStations()).isNotEmpty();
    }

    @Test
    void save() {
        final Line line = new Line("2호선");
        final Station station = stations.save(new Station("잠실역"));
        line.addStation(station);
        lines.save(line);
        lines.flush();
    }
}
