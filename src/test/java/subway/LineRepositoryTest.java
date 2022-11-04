package subway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineRepositoryTest {

    @Autowired
    StationRepository stations;

    @Autowired
    LineRepository lines;

    @Autowired
    TestEntityManager manager;

    @Test
    void findByName() {
        Line line = lines.findByName("3호선");

        assertThat(line.getStations()).isNotNull();
    }

    @Test
    void save() {
        //주인이 아닌 쪽은 읽기만 할 수 있다
        Line line = new Line("2호선");
        Station station = stations.save(new Station("잠실역"));
        line.addStation(station); //매핑 안됨...
        lines.save(line);
        flushAndClear();
    }


    @Test
    void identity() {
        Line line = lines.save(new Line("2호선"));
        Station station = stations.save(new Station("잠실역"));

        station.setLine(line);
        assertThat(line.getStations()).isNotEmpty();
    }

    @Test
    void update() {

    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }

}
