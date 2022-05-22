package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class LineStationRepositoryTest {
    @Autowired
    LineStationRepository lineStations;

    @Autowired
    StationRepository stations;

    @DisplayName("")
    @Test
    void saveWithLineStation() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역", lineStation));
    }
}