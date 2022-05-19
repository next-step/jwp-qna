package study.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("양방향 연관관계 - ")
class LineRepositoryTest {

    @Autowired
    private StationRepository stations;

    @Autowired
    private LineRepository lines;

    @Test
    @DisplayName("조회 테스트")
    void findById() {
        // given & when
        Line line = lines.findByName("3호선");

        // then
        assertThat(line.getStations()).hasSize(1);
    }

    @Test
    @DisplayName("저장 테스트 - 연관 관계 X")
    void nonAssociationSave() {
        // given
        Line expected = new Line("2호선");
        expected.addStation(new Station("잠실역"));

        // when & then
        lines.save(expected);
        lines.flush(); // transaction commit
    }

    @Test
    @DisplayName("저장 테스트 - 연관 관계 O")
    void associationSave() {
        // given
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));

        // when & then
        lines.save(expected);
        lines.flush(); // transaction commit
    }

    @Test
    @DisplayName("연관 관계 편의 메서드 테스트 - 라인 하나")
    void setOneLine() {
        // given
        Station station = new Station("잠실역");
        stations.save(station);

        Line line1 = new Line("2호선");
        lines.save(line1);

        station.setLine(line1);

        // when
        boolean contains = line1.getStations().contains(station); // true

        // then
        assertThat(contains).isTrue();
    }

    @Test
    @DisplayName("연관 관계 편의 메서드 테스트 - 라인 둘")
    void setTwoLine() {
        // given
        Station station = new Station("잠실역");
        stations.save(station);

        Line line1 = new Line("2호선");
        Line line2 = new Line("8호선");

        lines.save(line1);
        lines.save(line2);

        station.setLine(line1);
        station.setLine(line2);

        // when
        boolean contains = line1.getStations().contains(station); // Station -> setLine에 중복 제거가 있으면 false 없으면 true
        boolean contains2 = line2.getStations().contains(station); // true

        // then
        assertThat(contains).isFalse();
        assertThat(contains2).isTrue();
    }
}
