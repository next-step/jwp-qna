package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineStationRepository lineStations;

    @Test
    void save() {
        Station station = new Station("잠실역");
        Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        Station station1 = stations.save(new Station("잠실역"));
        Station stations2 = stations.findByName("잠실역");
        assertThat(stations2.getId()).isEqualTo(station1.getId());
        assertThat(stations2.getName()).isEqualTo(station1.getName());
        assertThat(stations2).isEqualTo(station1);
        assertThat(stations2).isSameAs(station1);
    }

    @Test
    void findById() {
        Station station1 = stations.save(new Station("잠실역"));
        Station stations2 = stations.findById(station1.getId()).get();
        assertThat(stations2).isSameAs(station1);
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLineStation() {
        LineStation lineStation = lineStations.save(new LineStation());
        stations.save(new Station("잠실역", lineStation));
    }

}