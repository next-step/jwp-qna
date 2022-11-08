package subway.repository;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Line;
import subway.domain.Station;

/**
*   테스트 시, jpa 관련 빈들만 등록해서 테스트
*/
@DataJpaTest
class StationRepositoryTest {

    @Autowired
    private StationRepository repository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    EntityManager em;

    @Test
    void save(){
        final Station station = new Station("잠실역");

        final Station actual = repository.save(station);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName(){
        Station station = new Station("몽촌토성역");
        repository.save(station);
        final Station actual = repository.findByName("몽촌토성역");
        assertThat(actual).isNotNull();
    }

    @Test
    void identity(){
        Station station = new Station(1L, "암사역");
        Station save = repository.save(station);
        Station actual = repository.findById(station.getId()).get();
        Station actual2 = repository.findById(station.getId()).get();

        assertThat(save == actual).isTrue();
    }

    @Test
    void update(){
        Station station = new Station("암사역");
        Station save = repository.save(station);
        save.changeName("강남역");
        repository.findByName("강남역");

        assertThat(save.getName()).isEqualTo("강남역");

    }

    @Test
    void saveWithLine(){
        final Station station = new Station("잠실역");
        station.setLine(lineRepository.save(new Line("2호선")));
        repository.save(station);

        flushAndClear();
    }

    @Test
    void findByNameWithLine(){
        Station station = repository.findByName("교대역");

        assertThat(station).isNotNull();
        assertThat(station.getLine()).isNotNull();
        assertThat(station.getLine().getName()).isEqualTo("3호선");
    }

    @Test
    void updateWithLine(){
        Line line = lineRepository.save(new Line("2호선"));
        Station station = repository.findByName("교대역");
        station.setLine(line);
        flushAndClear();
    }

    @Test
    void removeLine(){
        Station station = repository.findByName("교대역");
        station.setLine(null);
        flushAndClear();
    }

    @Test
    void removeLine2(){
        Line line = lineRepository.findById(1L).get();
        lineRepository.delete(line);
        flushAndClear();
    }

    private void flushAndClear(){
        em.flush();
        em.clear();
    }
}