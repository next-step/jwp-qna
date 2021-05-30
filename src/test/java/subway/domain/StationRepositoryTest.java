package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Test
    public void saveTest(){
        Station expected = new Station("잠실역");
        assertThat(expected.getId()).isNull();
        Station actual = stations.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    public void findByNameTest(){
        Station expected = new Station("잠실역");
        stations.save(expected);

        Station actual = stations.findByName("잠실역");
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station(1L, "잠실역"));
        station1.changeName("몽촌토성역");
        station1.changeName("잠실역");
        Station station2 = stations.findByName("잠실역");
        assertThat(station2).isNotNull();
    }

}