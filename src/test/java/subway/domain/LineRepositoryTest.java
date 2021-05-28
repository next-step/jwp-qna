package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LineRepositoryTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @AfterEach
    void clear() {
        stations.deleteAll();
    }

//    @Test
//    void saveWithLine_fail() {
//        Station expected = new Station("잠실역");
//        expected.setLine(new Line("2호선"));
//        Station actual = stations.save(expected);
//        stations.flush(); // transaction commit
//    }
    // 위 테스트는 안되고, 아래만 되는 이유는, 전부 영속 context 안에 있어야 flush 동작 가능하다.
    // -> JPA에서 엔티티를 저장할 때 연관된 모든 엔티티는 영속 상태여야 한다.
    @Test
    void saveWithLine_success() {
        Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        Station actual = stations.save(expected);
        stations.flush(); // transaction commit
    }

    @Test
    void findByNameWithLine() {
        Station expected = new Station(1L, "교대역");
        expected.setLine(lines.save(new Line(1L, "3호선")));
        stations.save(expected);
        System.out.println("=====================================================");

        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine() {
        Station expected = new Station(1L, "교대역");
        expected.setLine(lines.save(new Line(1L, "3호선")));
        stations.save(expected);
        System.out.println("=====================================================");

        expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush(); // transaction commit
    }

    @Test
    void removeLine() {
        Station expected = new Station(1L, "교대역");
        expected.setLine(lines.save(new Line(1L, "3호선")));
        stations.save(expected);
        System.out.println("=====================================================");

        expected = stations.findByName("교대역");
        expected.setLine(null);
        stations.flush(); // transaction commit
    }

//    @Test
//    void findById() {
//        Station expected = new Station(1L, "교대역");
//        expected.setLine(lines.save(new Line(1L, "3호선")));
//        stations.save(expected);
//        System.out.println("=====================================================");
//
//        Line line = lines.findByName("3호선");
//        assertThat(line.getStations()).isNotNull();
//        assertThat(line.getStations()).hasSize(1);
//    }
//
//    @Test
//    void save_1() {
//        Line expected = new Line("2호선");
//        expected.addStation(new Station("잠실역"));
//        lines.save(expected);
//        lines.flush(); // transaction commit
//    }
//
//    @Test
//    void save_2() {
//        Line expected = new Line("2호선");
//        expected.addStation(stations.save(new Station("잠실역")));
//        lines.save(expected);
//        lines.flush(); // transaction commit
//    }
}
