package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

@DisplayName("질문 목록에 대한 테스트")
class AnswersTest {

    private Answers answers;
    private Question question;
    private User writer;
    private User other;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        other = new User(2L, "other", "password", "name", "other@naver.com");
        question = new Question("test-title", "test-contents");

        answers = new Answers();
        answers.add(new Answer(writer, question, "contents-1"));
        answers.add(new Answer(writer, question, "contents-2"));
    }

    @DisplayName("다른 사람이 쓴 답변을 삭제하려하면 예외가 발생한다")
    @Test
    void z() {
        assertThatThrownBy(() -> {
            answers.delete(other);
        }).isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("본인이 작성한 답변을 삭제하려하면 정상적으로 삭제되어야 한다")
    @Test
    void z7() {
        answers.delete(writer);
        List<Answer> answerList = answers.getAnswers();

        assertAll(
            () -> assertTrue(answerList.get(0).isDeleted()),
            () -> assertTrue(answerList.get(1).isDeleted())
        );
    }
}
