package subway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

@DataJpaTest
public class LineTest {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LineRepository lineRepository;

    @Test
    public void saveWithLine (){
        //given
        Station station = new Station("잠실역");
        station.setLine(new Line("2호선"));
        stationRepository.save(station);
        //when

        //then
    }

    @Test
    public void findByNameWithLine (){
        Station station = stationRepository.findByName("교대역");
        Assertions.assertThat(station).isNotNull();
        Assertions.assertThat(station.getLine().getName()).isEqualTo("3호선");

    }

    @Test
    public void updateWithLine (){
        Station station = stationRepository.findByName("교대역");
        station.setLine(new Line("2호선"));
        System.out.println("flush start");
        stationRepository.flush();
        //when

        //then
    }

    @Test
    public void removeLine(){
        Station station = stationRepository.findByName("교대역");
        station.setLine(null);
        stationRepository.flush();
    }
}
