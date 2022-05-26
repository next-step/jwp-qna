package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

@DisplayName("답변에 대한 단위 테스트")
class AnswerTest {

    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        question = new Question("test-title", "test-contents");
    }

    @DisplayName("Answer 생성자에 writer 가 없다면 예외가 발생해야 한다")
    @Test
    void answer_exception_test() {
        assertThatThrownBy(() -> {
            new Answer(null, question, "contents");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("Answer 생성자에 question 가 없다면 예외가 발생해야 한다")
    @Test
    void answer_exception_test2() {
        assertThatThrownBy(() -> {
            new Answer(writer, null, "contents");
        }).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("다른 사람이 쓴 답변을 삭제하려하면 예외가 발생한다")
    @Test
    public void delete_test() {
        User other = new User(2L, "woobeen2", "other", "name", "drogba03@naver.com");
        Answer answer = new Answer(writer, question, "contents");

        assertThatThrownBy(() -> {
            answer.delete(other);
        }).isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("본인이 작성한 답변을 삭제하려하면 정상적으로 삭제되어야 한다")
    @Test
    public void delete_test2() {
        Answer answer = new Answer(writer, question, "contents");
        answer.delete(writer);

        assertTrue(answer.isDeleted());
    }

    @DisplayName("답변이 이미 삭제되었다면 예외가 발생해야 한다")
    @Test
    public void delete_test3() {
        Answer answer = new Answer(writer, question, "contents");
        answer.setDeleted(true);

        assertThatThrownBy(() -> {
            answer.delete(writer);
        }).isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining("이미 삭제된 답변이라 삭제할 수 없습니다.");
    }
}
