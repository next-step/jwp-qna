package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        LINE = lines.save(new Line("3호선"));
        STATION = new Station("교대역", LINE);
    }

    @Test
    void findById() {
        // given
        // when
        Line actual = lines.findByName("3호선");

        // then
        assertThat(actual.getStations()).hasSize(1);
    }

    @Test
    void findByNameWithLine() {
        // given
        stations.save(STATION);

        // when
        Station actual = stations.findByName("교대역");

        // then
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void removeLine() {
        // given
        stations.save(STATION);
        Station station = stations.findByName("교대역");

        // when
        station.removeLine();

        // then
        assertThat(station.getLine()).isNull();
    }

}
