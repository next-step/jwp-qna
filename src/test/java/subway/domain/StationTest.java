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
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertThat(station1.getId()).isEqualTo(station2.getId());
        assertThat(station1.getName()).isEqualTo(station2.getName());
        assertThat(station1).isEqualTo(station2);
        assertThat(station1).isSameAs(station2); // instance의 주소를 비교
    }

    @Test
    void findById() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
