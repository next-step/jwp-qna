package subway.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @Test
    public void saveWithLine() {
        Station expected = new Station("잠실역");
        expected.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(expected);
        stations.flush();
    }

    @Test
    public void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }


    @Test
    public void updateWithLine() {
        Station expected = new Station("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    void save() {
        Line expected = new Line("2호선");
        expected.addStation(stations.save(new Station("잠실역")));
        lines.save(expected);
        lines.flush(); // transaction commit
    }


}