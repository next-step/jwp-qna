package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
        // given
        Line line = lines.save(new Line("2호선"));
        Station station = stations.save(new Station("잠실역", line));

        // when
        LineStation lineStation = lineStations.save(new LineStation(line, station));

        // then
        assertThat(lineStation.getId()).isNotNull();
    }
}
