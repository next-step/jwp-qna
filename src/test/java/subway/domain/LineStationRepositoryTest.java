package subway.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineStationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineStationRepository lineStations;

    @Test
    void saveWithLineStation() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역", lineStation));
    }

}