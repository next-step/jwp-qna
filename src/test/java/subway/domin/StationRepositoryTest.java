package subway.domin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {
    @Autowired // JpaRepository를 상송받은 interface 는 자동으로 bean으로 인식
    private StationRepository stations;

    @Test
    void save() {
        Station expected = new Station("잠실역");
        assertThat(expected.getId()).isNull();

        Station actual = stations.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        Station expected = new Station("잠실역");
        stations.save(expected);

        Station actual = stations.findByName("잠실역");
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        station1.changeName("잠실역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
