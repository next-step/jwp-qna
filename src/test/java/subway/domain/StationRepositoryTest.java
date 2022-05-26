package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//데이터 베이스를 다루는 설정을 다 넣어준다
@DataJpaTest
class StationRepositoryTest {

    //JpaRepository 를  extends 하여 @NoRepositoryBean 을 하면 빈으로 자동 등록이 된다.
    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        final Station expected = new Station("잠실역");
        final Station actual = stations.save(expected);
        //아이다가 무슨 값인지는 중요하지 않고, 생겼다만 파악하면 됨
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
        final Station station1 = stations.save(new Station("잠실역"));
        //findByName으로 하면 Select를 하는데, findById로 하면 DB에 조회조차 하지 않음  (1차 캐시에서 본다 )
        final Station station2 = stations.findById(station1.getId()).get();
        // 동일 비교
        assertThat(station1 == station2).isTrue();
        assertThat(station1).isSameAs(station2);
    }


    @Test
    void save2() {
        // Spring 에서 어차피 DB에 저장하지 않을 것이기 때문에 안날림
        final Station actual = stations.save(new Station(1L, "잠실역"));
        // Select Query가 발생하는 이유는 아이디 값을 넣어서 주면 실제로 있는 지 없는지를 한번 DB에 체크함 ( save 구현체 보면 됨 )
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
        //이렇게 해줘야 직접 insert 날림
        stations.flush();
    }


    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        //(jpql) 바로 DB로 날리는 코드이다. 그래서 이전에 있는 값을 flush를 해서 최신화 한다.
        final Station stations2 = stations.findByName("몽촌토성역");
        assertThat(stations2).isNotNull();
    }

}