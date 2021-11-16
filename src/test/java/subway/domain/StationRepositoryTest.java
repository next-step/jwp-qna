package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;


@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Test
    void save() {
        // given
        String stationName = "잠실역";

        // when
        Station station = new Station(stationName);
        Station actual = stations.save(station);

        stations.flush();

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

        // then
        assertThat(actual.getId()).isEqualTo(expect.getId());
        assertThat(actual).isEqualTo(expect);
        assertThat(actual).isSameAs(expect);
    }

    @Test
    void update() {
        // given
        Station station1 = stations.save(new Station("잠실역"));

        // when
        station1.changeName("몽촌토성역");
        station1.changeName("잠실역");
        Station station2 = stations.findByName("몽촌토성역");

        // then
        assertThat(station2).isNull();
    }

    @Test
    void setLine() {
        // given
        Station station1 = stations.save(new Station("잠실역"));
        Line line = lines.save(new Line("3호선"));

        // when
        station1.setLine(line);
        Station actual = stations.findByName(station1.getName());

        // then
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

}
