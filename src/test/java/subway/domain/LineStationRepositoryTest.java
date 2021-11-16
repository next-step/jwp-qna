package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineStationRepositoryTest {

    @Autowired
    LineStationRepository lineStations;

    @Autowired
    StationRepository stations;

    @Autowired
    LineRepository lines;

    @Test
    void saveWithLineStation() {
        Line line = lines.save(new Line("2호선"));
        Station station = stations.save(new Station("잠실역"));
        LineStation lineStation = lineStations.save(new LineStation(line, station));

    }

    @Test
    void test() {
        Line line = lines.save(new Line("2호선"));
        Station station = new Station("잠실역", line);
        LineStation lineStation = lineStations.save(new LineStation(line, station));

        Station actualStation = stations.findByName("잠실역");
        LineStation actualLineStation = actualStation.getLineStation();

        assertAll(
            () -> assertThat(actualStation.getName()).isEqualTo("잠실역"),
            () -> assertThat(actualLineStation.getStation().getId()).isEqualTo(
                actualStation.getId())
        );
    }

}
