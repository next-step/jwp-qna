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

    @Autowired
    private LineRepository lines;

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

    @Test
    @DisplayName("연관 관계 설정 및 저장 테스트")
    void saveWithLine() {
        // given
        Station expected = new Station("잠실역");
        //expected.setLine(new Line("2호선"));        // JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다.
        expected.setLine(lines.save(new Line("2호선")));

        // when & then
        Station actual = stations.save(expected);
        stations.flush(); // transaction commit
    }

    @Test
    @DisplayName("조회 테스트")
    void findByNameWithLine() {
        // given & when
        Station actual = stations.findByName("교대역");

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    @DisplayName("수정 테스트")
    void updateWithLine() {
        // given
        Station expected = stations.findByName("교대역");

        // when & then
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush(); // transaction commit
    }

    /*@Test
    @DisplayName("연관 관계 제거 테스트")
    void removeLine() {
        // given
        Station expected = stations.findByName("교대역");

        // when & then
        expected.setLine(null);     // 노선을 삭제하려면 기존에 있던 연관 관계를 먼저 제거하고 삭제해야 한다.
        stations.flush(); // transaction commit
    }*/
}
