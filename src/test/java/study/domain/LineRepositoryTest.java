package study.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    LineRepository lines;

    @Autowired
    StationRepository stations;

    @Test
    @Sql(scripts = "classpath:sql/line_and_station_insert.sql")
    @DisplayName("호선으로 검색")
    void findByName() {
        Line line = lines.findByName("3호선");

        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    @DisplayName("라인을 저장 테스트 1")
    void save1() {
        Line expected = new Line("2호선");
        expected.addStation(new Station("잠실역"));
        lines.save(expected);
        lines.flush();

        Line actual = lines.findByName("2호선");
        assertAll(
                () -> assertThat(actual.getStations()).hasSize(1),
                () -> assertThat(actual.getStations().get(0).getId()).isNull()
        );
    }

    @Test
    @DisplayName("라인을 저장 테스트 2 (연관 관계의 주인이 아닌 곳에 입력된 값은 외래 키에 영향을 주지 않는다.)")
    void save2() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
        lines.flush();

        Line actual = lines.findByName("2호선");
        assertAll(
                () -> assertThat(actual.getStations()).hasSize(1),
                () -> assertThat(actual.getStations().get(0).getId()).isNotNull()
        );
    }
}
