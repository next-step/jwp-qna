package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import qna.config.TruncateConfig;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
class DeleteHistoryTest extends TruncateConfig {

    public static final DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.JAVAJIGI, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Autowired
    UserRepository users;

    @BeforeEach
    void setUp() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
    }

    @ParameterizedTest(name = "save_테스트")
    @MethodSource("deleteHistoryTestFixture")
    void save_테스트(DeleteHistory deleteHistory) {
        DeleteHistory saved = deleteHistories.save(deleteHistory);
        assertThat(saved.getCreateDate()).isNotNull();
    }

    @ParameterizedTest(name = "save_후_findById_테스트")
    @MethodSource("deleteHistoryTestFixture")
    void save_후_findById_테스트(DeleteHistory deleteHistory) {
        DeleteHistory history1 = deleteHistories.save(deleteHistory);
        DeleteHistory history2 = deleteHistories.findById(history1.getId()).get();
        assertThat(history1).isEqualTo(history2);
    }

    static Stream<DeleteHistory> deleteHistoryTestFixture() {
        return Stream.of(DH1, DH2);
    }
}