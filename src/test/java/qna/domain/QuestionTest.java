package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    @Test
    @DisplayName("질문에 답글을 추가하는 경우, 답글의 질문도 바뀐다")
    void addAnswer() {
        Question question = new Question(2L, "title", "contents");
        Answer answer = new Answer(1L, new User(), question, null);

        question.addAnswer(answer);

        assertAll(
            () -> assertThat(answer.getQuestion().getId()).isEqualTo(question.getId()),
            () -> assertThat(question.getAnswers().size()).isEqualTo(1),
            () -> assertThat(question.getAnswers().get(0).getId()).isEqualTo(answer.getId())
        );
    }

    @Test
    @DisplayName("자신의 질문만 삭제할 수 있다")
    void delete1() throws CannotDeleteException {
        User writer = new User(1L, "1", "password", "user1", "test@email.com");
        Question question = new Question("title", "contents");
        question.setWriter(writer);

        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("자신의 질문이 아닌 경우 삭제 권한이 없다.")
    void delete2() {
        User writer = new User(1L, "1", "password", "user1", "test@email.com");
        TestDummy.QUESTION1.setWriter(writer);

        User other = new User(2L, "2", "password", "user2", "test2@email.com");

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(
                () -> TestDummy.QUESTION1.delete(other))
            .withMessage("질문을 삭제할 권한이 없습니다.");
    }
}
