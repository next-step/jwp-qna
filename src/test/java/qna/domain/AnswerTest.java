package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {

    @Test
    @DisplayName("답글의 질문을 바꾸는 경우, 질문에서도 답글이 바뀐다")
    void changeQuestion() {
        Question question = new Question(2L, "title", "contents");
        Answer answer = new Answer(1L, new User(), question, null);

        answer.setQuestion(question);

        assertAll(
            () -> assertThat(answer.getQuestion().getId()).isEqualTo(question.getId()),
            () -> assertThat(question.getAnswers().getValues().size()).isEqualTo(1),
            () -> assertThat(question.getAnswers().getValues().get(0).getId()).isEqualTo(answer.getId())
        );
    }

    @Test
    @DisplayName("답글의 작성자만 삭제할 수 있다")
    void delete1() throws CannotDeleteException {
        User writer = userOf(1L);
        Answer answer = new Answer(writer, new Question(), "contents");

        User loginUser = userOf(1L);
        answer.delete(loginUser);

        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답글의 작성자가 아닌 경우 삭제시 예외 발생")
    void delete2() {
        User writer = userOf(1L);
        Answer answer = new Answer(writer, new Question(), "contents");

        User loginUser = userOf(2L);

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(
                () -> answer.delete(loginUser))
            .withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    private User userOf(Long id) {
        return new User(id, "userId", "password", "name", "email");
    }
}
