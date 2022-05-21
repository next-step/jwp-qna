package subway;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.StationRepository;

@DataJpaTest // @Transactional, Rollback
public class StationRepositoryTest {

    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void save() {
        //given
        Station expected = new Station("잠실역");

        //when
        Station saved = stationRepository.save(expected);

        //then
        assertThat(saved.getName()).isEqualTo("잠실역");
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void findByName() {
        //given
        Station expected = new Station("잠실역");
        stationRepository.save(expected);
        //when
        Station actual = stationRepository.findByName("잠실역");
        assertThat(actual).isEqualTo(expected);

        //then
    }

    @Test
    public void identity() {
        //given
        Station station1 = new Station("잠실역");
        Station s1 = stationRepository.save(station1);
        Station s2 = stationRepository.findByName("잠실역");
        stationRepository.findById(station1.getId());
        assertThat(s1 == s2).isTrue();
    }

    @Test
    @Transactional
    public void update() {
        //given
        Station 잠실역 = new Station("잠실역");
        stationRepository.save(잠실역);

        잠실역.changeName("몽촌토성");

//        Station 몽촌토성 = stationRepository.findByName("몽촌토성");
//
//        assertThat(몽촌토성).isNotNull();

        //then
    }
}
