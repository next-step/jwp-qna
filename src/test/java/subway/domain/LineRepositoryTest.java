package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class LineRepositoryTest {
    @Autowired
    private LineRepository lines;

    @Autowired
    private StationRepository stations;

    @DisplayName("모든 엔티티는 영속화된 상태여야 한다")
    @Test
    void saveWithLine() {
        Station expected = new Station("잠실역");
        // expected.changeLine(new Line("2호선"));
        expected.changeLine(lines.save(new Line("2호선")));
        Station actual = stations.save(expected);
        stations.flush(); // transaction commit
    }

    @DisplayName("객체는 자유롭게 객체 그래프를 탐색할 수 있어야 한다")
    @Test
    void findByNameWithLine() {
        Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine().getName()).isEqualTo("3호선");
    }

    @DisplayName("한 트랜잭션 안에서 발생하는 엔티티의 변경사항을 감지한다")
    @Test
    void updateWithLine() {
        Station expected = stations.findByName("교대역");
        expected.changeLine(lines.save(new Line("2호선")));
        stations.flush(); // transaction commit
    }

    @DisplayName("참조에 NULL을 사용하면 연관된 엔티티를 삭제할 수 있다")
    @Test
    void removeLine() {
        Station expected = stations.findByName("교대역");
        expected.changeLine(null);
        stations.flush(); // transaction commit
    }
}
