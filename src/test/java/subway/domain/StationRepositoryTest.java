package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;


    @Test
    void save() {
        final Station expected = new Station("잠실역");
        final Station actual = stations.save(expected);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        stations.save(new Station("잠실역"));
        final Station actual = stations.findByName("잠실역");
        assertThat(actual).isNotNull();
    }

    @Test
    void identity() {
        final Station station = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");

        //동일성 보장 == 비교
        assertThat(station).isSameAs(station2);
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        //커밋을 찍는다
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isEqualTo(station1);

    }

    @Test
    void removeLine() {
        final Line saveLine = lines.save(new Line("3호선"));
        final Station station = stations.save(new Station("교대역"));
        station.setLine(saveLine);
        station.removeLine();
        //커밋을 찍는다
        stations.flush();

    }
}