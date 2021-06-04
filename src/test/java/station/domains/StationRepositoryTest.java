package station.domains;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;
    @Autowired
    private LineRepository lines;

    @Test
    void save() {
        Station expected = new Station("잠실역");
        Station actual = stations.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        String expected = "잠실역";
        stations.save(new Station(expected));
        String actual = stations.findByName(expected).getName();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void identity() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findById(station1.getId()).get();
        assertThat(station1 == station2).isTrue();
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLine_err() {
        Station expected = new Station("잠실역");
        expected.setLine(new Line("2호선"));
        Station actual = stations.save(expected);
        //stations.flush(); // transaction commit
    }

    @Test
    void saveWithLine() {
        Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(expected);
        stations.flush(); // transaction commit
    }

    @Test
    void findByNameWithLine() {
        Station station = new Station("교대역");
        station.setLine(lines.save(new Line("3호선")));
        stations.save(station);

        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush(); // transaction commit
    }

}
