package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("삭제 히스토리에 목록 대한 단위 테스트")
class DeleteHistoriesTest {

    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        question = new Question("test-title", "test-contents");
    }

    @DisplayName("삭제 히스토리 객체에 단건을 더하면 정상적으로 더해져야 한다")
    @Test
    void z() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(
            new Question(1L, "test-title", "test-contents"));

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(deleteHistory);

        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);

    }

    @DisplayName("삭제 히스토리 객체에 리스트를 더하면 정상적으로 더해져야 한다")
    @Test
    void z2() {
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(
            new Answer(1L, writer, question, "contents"));
        DeleteHistory deleteHistory2 = DeleteHistory.ofAnswer(
            new Answer(2L, writer, question, "contents"));
        DeleteHistory deleteHistory3 = DeleteHistory.ofAnswer(
            new Answer(3L, writer, question, "contents"));

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addAll(Arrays.asList(deleteHistory, deleteHistory2, deleteHistory3));

        assertThat(deleteHistories.getDeleteHistories()).hasSize(3);
    }
}
