package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:subway/application.properties")
public class LineRepositoryTest {

    @Autowired
    private LineRepository lines;


    @Autowired
    private StationRepository stations;

    @Test
    void saveWithLine() {
        Station expect = new Station("잠실역");
        expect.setLine(lines.save(new Line("2호선"))); // 영속상태여야 한다.
        Station actual = stations.save(expect);
        stations.flush(); // commit
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

//    @Test
//    void removeLine() {
//        Station station = stations.findByName("교대역");
//        station.setLine(null);
//        stations.flush();
//    }

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
