package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LineTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    void findByNameWithLine() {
        final Station station = stations.findByName("잠실역");
        assertThat(station).isNotNull();
    }
}
