package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("삭제 히스토리에 대한 단위 테스트")
class DeleteHistoryTest {

    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        question = new Question(1L, "test-title", "test-contents");
    }

    @DisplayName("Answer 객체를 넘기면 Answer 에 해당하는 값이 DeleteHistory 에 저장되어 생성되어야 한다")
    @Test
    void create_delete_history_test() {
        Answer answer = new Answer(1L, writer, question, "contents");
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(answer);

        assertAll(
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(answer.getId()),
            () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(answer.getWriter())
        );
    }

    @DisplayName("Question 객체를 넘기면 Question 에 해당하는 값이 DeleteHistory 에 저장되어 생성되어야 한다")
    @Test
    void create_delete_history_test2() {
        question = question.writeBy(writer);
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);

        assertAll(
            () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
            () -> assertThat(deleteHistory.getContentId()).isEqualTo(question.getId()),
            () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(question.getWriter())
        );
    }
}
