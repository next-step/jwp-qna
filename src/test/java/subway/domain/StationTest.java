package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationTest {

    @Autowired
    private StationRepository stations;

    @Test
    public void save() {
        // begin
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("잠실역");
        // commit
        // 왜 commit 이 되기 전에 insert 쿼리가 먼저 나갔을까? -> 데이터베이스의 식별자 생성전략과 관련이 있다.
        // Identity(Auto Increment) 는 식별자를 생성하기 위해서 DB를 한번 조회를 해야한다.
        // 그래서 쓰기 지연과 관련 없이 데이터베이스에 저장을 하게 된다.
        // 만약 Identity 가 아니라 식별자를 관리하는 테이블을 별도로 만든다면... 쓰기지연이 일어난다.
    }

    @Test
    public void save_delay() {
        // begin
        final Station station = new Station(3L, "잠실역"); // @GeneratedValue(strategy = GenerationType.IDENTITY) 삭제하고 실행해볼 것
        final Station actual = stations.save(station); // insert 가 아니라 select 쿼리로 나간다. -> id가 있으면 persist 가 아니라 merge 방식으로 동작
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    public void findByName() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findByName("잠실역");

        Assertions.assertThat(station2.getId()).isEqualTo(station1.getId());
        Assertions.assertThat(station2.getName()).isEqualTo(station1.getName());

        Assertions.assertThat(station2).isEqualTo(station1); // 동등성
        Assertions.assertThat(station2).isSameAs(station1); // 동일성 - 주소값 비교
    }

    @Test
    public void findById() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findById(station1.getId()).get();
        Assertions.assertThat(station2).isSameAs(station1);
    }

    @Test
    public void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
//        station1.changeName("잠실역"); // 변화된 내용이 없어서 update 쿼리가 실행이 안된다.
        Station station2 = stations.findByName("몽촌토성역");
        Assertions.assertThat(station2).isNotNull();
    }
}
