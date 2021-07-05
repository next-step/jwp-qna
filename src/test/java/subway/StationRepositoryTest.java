package subway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Station;
import subway.domain.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Test
    public void save() {
        Station expected = new Station("신림역");
        assertThat(expected.getId()).isNull();

        Station actual = stations.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("신림역");
    }

    @Test
    public void findByName() {
        Station expected = new Station("신림역");
        stations.save(expected);

        Station actual = stations.findByName("신림역");
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("신림역");
        assertThat(actual).isSameAs(expected);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("신림역"));
        station1.changeName("낙성대역");
//        station1.changeName("신림역");
        Station station2 = stations.findByName("낙성대역");
        assertThat(station2).isNotNull();
    }
}
