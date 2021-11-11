package subway;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Station;
import subway.domain.StationRepository;

@DataJpaTest
class StationTest {
    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertThat((station2.getId())).isEqualTo(station1.getId());
        assertThat((station2.getName())).isEqualTo(station1.getName());
        assertThat(station2).isEqualTo(station1);
        assertThat(station2).isSameAs(station1);
    }

    @Test
    void findById() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
        assertThat((station2.getId())).isEqualTo(station1.getId());
        assertThat((station2.getName())).isEqualTo(station1.getName());
        assertThat(station2).isEqualTo(station1);
        assertThat(station2).isSameAs(station1);
    }

    @Test
    void update() {
        final Station station = stations.save(new Station("잠실역"));
        station.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}