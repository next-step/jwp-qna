package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Test
    void name() {
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

//        Station actual = stations.findById(1L).get();
        Station actual = stations.findByName("잠실역");
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");

        assertThat(actual).isSameAs(expected); // 주소값마저 같은것
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        // 10000줄 코드
        station1.changeName("잠실역");
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }

    @Test
    void persist_merge() {
        Station station1 = stations.save(new Station("잠실역")); // persist 비영속 -> 영속
        Station station2 = new Station(station1.getId(), "몽촌토성역"); // 1차 캐시
        Station station3 = stations.save(station2); // merge 준영속 -> 영속 이지만 해당 엔티티는 영속성컨텍스트완 다르기 때문에 이전 값 가져와서 넣어준다.
        assertThat(station3).isSameAs(station1);
    }

    @Test
    void save_line() {
        Station station = stations.save(new Station("잠실역"));
        Line line = lines.save(new Line("2호선"));
        station.setLine(line);
        stations.flush();
        assertThat(line.getStations().size()).isEqualTo(1);
    }
}