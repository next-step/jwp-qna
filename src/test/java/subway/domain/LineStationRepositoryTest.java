package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LineStationRepositoryTest {

    @Autowired
    private LineStationRepository lineStations;

    @Autowired
    private StationRepository stations;

    @Test
    @DisplayName("저장 테스트")
    void saveWithLineStation() {
        // given
        LineStation lineStation = lineStations.save(new LineStation());

        // when & then
        stations.save(new Station("잠실역", lineStation));
    }


}
