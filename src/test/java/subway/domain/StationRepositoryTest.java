package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stationRepository;

    @Test
    void save() {
        final Station expected = new Station("잠실역");
        final Station save = stationRepository.save(expected);
        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo("잠실역");
    }

    @Test
    void findById() {
        stationRepository.save(new Station("잠실역"));
        final Station actual = stationRepository.findByName("잠실역");
        assertThat(actual).isNotNull();
    }

    @Test
    void identity() {
        final Station station1 = stationRepository.save(new Station("잠실역"));
        final Station station2 = stationRepository.findById(station1.getId()).get();
        assertThat(station1 == station2).isTrue();
        assertThat(station1).isSameAs(station2);
    }

    @Test
    void update() {
        Station station1 = stationRepository.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stationRepository.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
