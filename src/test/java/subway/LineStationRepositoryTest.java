package subway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.LineStation;
import subway.domain.LineStationRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

@DataJpaTest
public class LineStationRepositoryTest {

    @Autowired
    private LineStationRepository lineStations;

    @Autowired
    private StationRepository stations;

    @Test
    void saveWithLineStation() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역", lineStation));
    }
}
