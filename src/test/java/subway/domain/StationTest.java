package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class StationTest {
    @Autowired
    private StationRepository stations; // s는 학습적 허용

    @DisplayName("station 저장")
    @Test
    void save() {
        final Station station = new Station("잠실역");
        final Station actual = stations.save(station);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("잠실역")
        );
    }

    @DisplayName("station 이름으로 조회")
    @Test
    void findByName() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertThat(station1.getId()).isEqualTo(station2.getId());
        assertThat(station1.getName()).isEqualTo(station2.getName());
        assertThat(station1).isEqualTo(station2);
        assertThat(station1).isSameAs(station2); // instance의 주소를 비교
    }

    @DisplayName("station Id로 조회")
    @Test
    void findById() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get();
        // select 쿼리가 일어나지 않음. 캐시에서 가져오므로
    }

    @DisplayName("station update")
    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        final Station station2 = stations.findByName("몽촌토성역");
        assertThat(station2).isNotNull();
    }
}
