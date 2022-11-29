package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    //flush, clear 등을 실행하고 싶다면
    //TestEntityManager, EntityManager 둘 중 하나 주입해서 사용
    @Autowired
    private TestEntityManager manager;
    /*@Autowired
    private EntityManager manager;*/

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        //System.out.println("debug point");
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void save2() {
        final Station station = new Station(1L, "잠실역");
        //@GeneratedValue(strategy = GenerationType.IDENTITY)이면 save할때 id를 자동생성, 입력하기 위해 DB insert 일어남
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
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
        // transaction begin
        final Station station1 = stations.save(new Station("잠실역"));
        //final Station station2 = stations.findByName("잠실역");
        final Station station2 = stations.findById(station1.getId()).get();
        assertThat(station1 == station2).isTrue();
        // transaction commit
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        //findByName >> id 기준으로 검색하는 게 아니기 때문에 영속성 컨텍스트에서 찾지않고
        //JPQL을 사용해서 검색
        //JPQL을 실행할 때에는 실행 전에 flush를 해줌
        //그래서 transaction commit 이전임에도 findByName 실행하면 update문 실행되고 난 후 select문 실행되는 것
        //final Station station2 = stations.findById(station1.getId()).get();
        //findByName 대신 findById로 검색하면 flush 일어나지 않아서 update문도 실행되지 않음
        //@DataJpaTest 에서는 테스트가 끝나면 rollback 하기 때문에 마지막에 commit 안함 (어짜피 rollback 할거니까)
        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLine() {
        final Station station = new Station("잠실역");
        station.setLine(lines.save(new Line("2호선")));
        stations.save(station);
        flushAndClear();
    }

    @Test
    void findByNameWithLine() {
        final Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine()).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        final Line line = lines.save(new Line("2호선"));
        final Station station = stations.findByName("교대역");
        station.setLine(line);
        flushAndClear();
    }

    @Test
    void removeLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(null);
        flushAndClear();
    }

    @Test
    void deleteLine() {
        final Station station = stations.findByName("교대역");
        station.setLine(null);
        final Line line = lines.findByName("3호선");
        lines.delete(line);
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}