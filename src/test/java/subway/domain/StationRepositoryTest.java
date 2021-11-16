package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        // given
        String stationName = "잠실역";

        // when
        Station expected = new Station(stationName);
        Station actual = stations.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(stationName)
        );
    }

    @Test
    void findByName() {
        // given
        String stationName = "잠실역";

        // when
        Station actual = stations.save(new Station(stationName));
        Station expect = stations.findByName(actual.getName());

        assertThat(actual.getId()).isEqualTo(expect.getId());
        assertThat(actual).isEqualTo(expect);
        assertThat(actual).isSameAs(expect);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        station1.changeName("잠실역");
        Station station2 = stations.findByName("몽촌토성역");

        assertThat(station2).isNull();
    }

}
