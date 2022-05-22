package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StationRepositoryTest {
    @Autowired
    private StationRepository stations; //jpaRepository 상속으로 자동 bean 등록

    @Test
    void save() {
        final Station expected =  new Station("잠실역");
        final Station actual = stations.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    public void findByName() {
        stations.save(new Station("잠실역"));
        final Station actual = stations.findByName("잠실역");
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("같은 인스턴스 임을 보장(동일성)")
    void identity() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        final Station station3 = stations.findById(station1.getId()).get(); // sql은 실행되지 않는다, 1차 캐시에 이미 있기때문
        assertThat(station1).isSameAs(station2);
        //assertThat(station1 == station2).isTrue();
        //assertThat(station1.equals(station2)).isTrue();
    }

//    @Test
//    void save() {
//        final Station actual = stations.save(new Station(1L,"잠실역")); //save안에 isNew로 있는지 없는지 보기위해 select가 한 번 호출된다.
//        assertThat(actual.getId()).isNotNull();
//        assertThat(actual.getName()).isEqualTo("잠실역");
//        stations.flush(); //@DataJpaTest에선 쓰기지연일때 flush안하면 굳이 insert쿼리 날리지않음
//    }

    @Test
    @DisplayName("변경 감지 (dirty checking)")
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        // 쓰기 지연임에도 flush가 되어 update 쿼리가 된 이유 -> findByName때문에 (DB 최신 데이터로) jpql이 동작
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}