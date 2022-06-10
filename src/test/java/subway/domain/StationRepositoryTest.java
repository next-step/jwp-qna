package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql(value = {"classpath:db/subway/truncate.sql"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class StationRepositoryTest {
    @Autowired
    private StationRepository stations;

    @DisplayName("지하철역을 저장한다")
    @Test
    void save() {
        Station expected = new Station("잠실역");
        Station actual = stations.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @DisplayName("이름으로 지하철역을 찾는다")
    @Test
    void findByName() {
        String expected = "잠실역";
        stations.save(new Station(expected));
        String actual = stations.findByName(expected).getName();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("아이디로 지하철역을 찾는다")
    @Test
    void identity() {
        Station station1 = stations.save(new Station("잠실역"));
        Station station2 = stations.findById(station1.getId()).get();
        assertThat(station1).isSameAs(station2);
    }

    @DisplayName("지하철역을 영속성 컨텍스트에 등록한다")
    @Test
    void register() {
        Station actual = stations.save(new Station(1L, "잠실역")); // select sql이 실행되는 이유는?
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
        // stations.flush();
    }

    @DisplayName("바뀐 이름으로 지하철역을 찾는다")
    @Test
    void update() {
        Station station1 = stations.save(new Station("잠실역"));
        station1.setName("몽촌토성역");
        Station station2 = stations.findByName("몽촌토성역"); // update sql이 실행되는 이유는?
        assertThat(station2).isNotNull();
    }
}
