package study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Test
    @DisplayName("지하철역 저장 테스트")
    void save() {
        // given
        Station expected = new Station("잠실역");

        // when
        Station actual = stations.save(expected);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("지하철역 조회 테스트")
    void findByName() {
        // given
        String expected = "잠실역";
        stations.save(new Station(expected));

        // when
        String actual = stations.findByName(expected).getName();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("동일성 보장 맛보기")
    void identity() {
        // given
        Station station1 = stations.save(new Station("잠실역"));

        // when
        Station station2 = stations.findById(station1.getId()).get();

        // then
        assertThat(station1 == station2).isTrue();
    }

    @Test
    @DisplayName("변경 감지 맛보기")
    void update() {
        // given
        Station station1 = stations.save(new Station("잠실역"));

        // when
        station1.changeName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역");
        Station station3 = stations.findByName("잠실역");

        // then
        assertThat(station2).isNotNull();
        assertThat(station3).isNull();
    }
}
