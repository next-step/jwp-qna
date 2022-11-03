package subway;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import javax.persistence.EntityManager;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    StationRepository stations;

    @Autowired
    LineRepository lines;

    @Autowired
    TestEntityManager manager;

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station); // 왜 다시 담아야 할까? PERSIST가 작동하면 바로 리턴 but merge는 nope
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("잠실역");


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

        //영속성 컨텍스트가 가지고 있으면 셀렉트 실행 안함
        Station station1 = stations.save(new Station("잠실역"));
//        final Station station2 = stations.findByName("잠실역");
        Station station3 = stations.findById(station1.getId()).get();

//        assertThat(station1 == station2).isTrue();
        assertThat(station1 == station3).isTrue();
    }

    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
//        Station station3 = stations.findById(station1.getId()).get();
        Station station2 = stations.findByName("몽촌토성역");
        assertThat(station1 == station2).isTrue();
    stations.flush();


    }

    @Test
    void saveWithLine() {
        Station station = new Station("잠실역");

        station.setLine(lines.save(new Line("2호선")));
        stations.save(station);

        flushAndClear();
    }

    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine()).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine(){
        Line  line = lines.save(new Line("2호선"));
        Station station = new Station("교대역");
        station.setLine(line);
        flushAndClear();
    }


    @Test
    void removeLine(){
        Line  line = lines.findById(1L).get();
        lines.delete(line);
        flushAndClear();
    }


    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}
