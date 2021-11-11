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
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        Assertions.assertThat(actual.getId()).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    public void findByName() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findByName("잠실역");

        Assertions.assertThat(station2.getId()).isEqualTo(station1.getId());
        Assertions.assertThat(station2.getName()).isEqualTo(station1.getName());
        Assertions.assertThat(station2).isEqualTo(station1);
        Assertions.assertThat(station2).isSameAs(station1);
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
        station1.changeName("잠실역"); // 변화된 내용이 없어서 update 쿼리가 실행이 안된다.
        Station station2 = stations.findByName("몽촌토성역");
        Assertions.assertThat(station2).isNotNull();
    }
}
