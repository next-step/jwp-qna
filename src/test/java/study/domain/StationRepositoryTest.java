package study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

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

    @Test
    @DisplayName("라인과 함께 저장")
    void saveWithLine() {
        Station expected = new Station("잠실역");
        expected.setLine(new Line("2호선"));
        Station actual = stations.save(expected);
        stations.flush();

        assertThat(actual.getLine().getName()).isEqualTo("2호선");
    }

    @Test
    @Sql(scripts = {"classpath:sql/line_and_station_insert.sql"})
    @DisplayName("역 조회 후 호선 검증")
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getLine().getName()).isEqualTo("3호선")
        );
    }

    @Test
    @Sql(scripts = {"classpath:sql/line_and_station_insert.sql"})
    @DisplayName("역 조회 후 수정")
    void updateWithLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush();

        assertThat(expected.getLine().getName()).isEqualTo("2호선");
    }

    @Test
    @Sql(scripts = {"classpath:sql/line_and_station_insert.sql"})
    @DisplayName("연관 관계 제거")
    void removeLine() {
        Station expected = stations.findByName("교대역");
        expected.setLine(null);
        stations.flush();

        assertThat(expected.getLine()).isNull();
    }
}
