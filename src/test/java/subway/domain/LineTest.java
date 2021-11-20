package subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@DataJpaTest
public class LineTest {

    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    public void saveWithLine() {
        Station expected = new Station("잠실역");
        Line line = new Line("2호선");

        Assertions.assertThatThrownBy(() -> {
                    expected.setLine(line);
                    Station actual = stations.save(expected); // 해당 쿼리는 실패한다. 왜냐하면 Line 객체가 영속성 컨텍스트에 있지 않기 때문이다.
                    stations.flush(); // commit 과 같은 것이라고 보면 된다.
                }
        ).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void saveWithLine_success() {
        Station expected = new Station("잠실역");
        Line line = new Line("2호선");

        expected.setLine(lines.save(line)); // line 객체를 영속성 상태로 만든다.
        Station actual = stations.save(expected);
        stations.flush(); // commit 과 같은 것이라고 보면 된다.
        //Assertions.assertThat(actual).isEqualTo(expected);
    }


    @Test
    public void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    public void updateWithLine() {
        Station station = stations.findByName("교대역");
        station.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    public void removeLine() {
        Station station = stations.findByName("교대역");
        station.setLine(null); // 연관관계 제거
        stations.flush();
    }

    @Test
    public void findById() {
        Line line = lines.findByName("2호선");
        Assertions.assertThat(line.getStations()).hasSize(1);
    }

    @Test
    public void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        Assertions.assertThat(expected.getStations().size()).isEqualTo(1);

        lines.save(expected);
        lines.flush();




    }
}
