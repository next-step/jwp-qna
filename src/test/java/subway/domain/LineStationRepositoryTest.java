package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LineStationRepositoryTest {
    @Autowired
    private LineStationRepository lineStations;

    @Autowired
    private StationRepository stations;


    @Test
    void saveWithLineStation() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역", lineStation));
    }


    @Test
    void saveWithLineStation_linstaion에_외래키() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역"));
    }
}