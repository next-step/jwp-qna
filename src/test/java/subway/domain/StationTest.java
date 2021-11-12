package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StationTest {
    @Autowired
    private StationRepository stations;

    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    //같은 트랜잭션에 있는 경우 영속 컨텍스트 특성으로 인해 1차 캐시에서 값을 찾아와서 반환함
    //1차 캐시에 없는 경우 DB에서 조회 후 결과를 1차 캐시에 저장하고 값을 반환함
    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertThat(station1.getId()).isEqualTo(station2.getId());
        assertThat(station1.getName()).isEqualTo(station2.getName());
        assertThat(station1).isEqualTo(station2);
        assertThat(station1).isSameAs(station2);
    }

    //캐시에서 값을 찾아오는 기준은 ID 임
    //ID로 조회할 경우 select 쿼리가 실행되지 않음... (1차 캐시에 존재하기 때문에)
    @Test
    void findById() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
        assertThat(station1).isSameAs(station2);
    }

    //insert 쿼리를 날리는 경우 1차 캐시에 먼저 저장되고 SQL 저장소에 보관
    //트랜잭션 커밋이 일어나는 경우 SQL 저장소에 있던 것들을 flush하면서 DB로 커밋됨
    //auto increment, identity 등 식별자를 조회하는 경우 쓰기지연과 관계없이 DB로 바로 전달됨
    //트랜잭션 커밋이 일어나기 전 DB에 바로 접근하는 쿼리가 있는 경우(findByName 등) 영속성 컨텍스트 flush 발생됨

    @Test
    void update(){
        final Station station1 = stations.save(new Station("잠실역")); //영속상태가 됨
        station1.changeName("몽촌토성역"); //영속상태인 엔티티의 값을 변경하면 해당 값에 대한 update 쿼리가 자동 실행됨
        //왜 실행되느냐??
        //1차 캐시에는 처음 영속상태가 될 때의 엔티티의 스냅샷이 저장됨.
        //트랜잭션이 커밋되고 난 후 스냅샷과 현재의 엔티티를 비교하여 차이가 있는 경우 update 쿼리가 생성되고 DB에 반영됨
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
