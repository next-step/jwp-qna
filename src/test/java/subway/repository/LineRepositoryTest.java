package subway.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Line;
import subway.domain.Station;

@DataJpaTest
class LineRepositoryTest {

    @Autowired
    private LineRepository repository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    EntityManager em;

    @Test
    void findByName(){
        Line line = repository.findByName("3호선");

        assertThat(line.getStations()).isNotNull();
    }

    @Test
    void save(){
        Line line = new Line("2호선");
        Station station = stationRepository.save(new Station("잠실역"));
        line.add(station);
        repository.save(line);
        flushAndClear();
    }

    @Test
    void name(){
        Line line = repository.save(new Line("2호선"));
        Station station = stationRepository.save(new Station("잠실역"));
        station.setLine(line);

        flushAndClear();

        assertThat(line.getStations().size()).isEqualTo(1);
    }

    private void flushAndClear(){
        em.flush();
        em.clear();
    }
}