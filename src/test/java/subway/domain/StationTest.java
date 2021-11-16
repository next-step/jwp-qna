package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StationTest {
    @Autowired
    private StationRepository stations;

    @Test
    void name() {
        final Station station = new Station("뚝섬역");
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("뚝섬역");
    }

    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("뚝섬역"));
        final Station station2 = stations.findByName("뚝섬역");
        assertThat(station2.getId()).isEqualTo(station1.getId());
        assertThat(station2.getName()).isEqualTo(station1.getName());
        assertThat(station2).isEqualTo(station1);
        assertThat(station2).isSameAs(station1);
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("뚝섬역"));
        station1.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
