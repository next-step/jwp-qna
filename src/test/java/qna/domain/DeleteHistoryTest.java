package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryTest {

    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, 1L, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("saveTestFixture")
    void save_테스트(DeleteHistory deleteHistory) {
        DeleteHistory history1 = deleteHistories.save(deleteHistory);
        assertThat(history1).isEqualTo(deleteHistory);
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("saveTestFixture")
    void save_후_findById_테스트(DeleteHistory deleteHistory) {
        DeleteHistory history1 = deleteHistories.save(deleteHistory);
        DeleteHistory history2 = deleteHistories.findById(history1.getId()).get();
        assertThat(history1).isEqualTo(history2);
    }

    static Stream<DeleteHistory> saveTestFixture() {
        return Stream.of(DH1, DH2);
    }
}