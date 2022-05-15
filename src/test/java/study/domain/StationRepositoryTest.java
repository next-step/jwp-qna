package study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Test
    @DisplayName("저장이 잘되는지 확인")
    void save() {
        Station expected = new Station("잠실역");
        Station actual = stations.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("저장한 후 역이름으로 잘 조회되는지 확인")
    void findByName() {
        String expected = "잠실역";
        stations.save(new Station(expected));
        String actual = stations.findByName(expected).getName();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("동일성 보장 확인")
    void identity() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findById(station1.getId()).get();

        assertThat(station1 == station2).isTrue();
    }

    @Test
    @DisplayName("변경 감지 맛보기")
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");

        assertThat(station2).isNotNull();
    }
}
