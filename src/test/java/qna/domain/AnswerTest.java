package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

class AnswerTest {
    Question question;
    Answer answer;
    User writer;

    @BeforeEach
    void setUp() {
        UserAuth userAuth = new UserAuth("user", "password");
        writer = new User(userAuth, "name", "email@email.com");
        question = new Question("title", "contents").writeBy(writer);
        answer = new Answer(question.getWriter(), question, "contents");
    }

    @Test
    @DisplayName("답변 생성시 작성자가 없으면 UnAuthorizedException 예외 던지기")
    void create_answer_writer_is_null_throw_UnAuthorizedException() {
        assertThatThrownBy(() -> new Answer(null, new Question("title", "contents"), "contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("답변 생성시 질문이 없으면 NotFoundException 예외 던지기")
    void create_answer_question_is_null_throw_NotFoundException() {
        assertThatThrownBy(() -> new Answer(writer, null, "contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("답변의 작성자이 아닐 경우 CannotDeleteException 예외 던지기")
    void is_not_writer_throw_CannotDeleteException() {
        User loginUser = new User(new UserAuth("user2", "password"), "name2", "email2@email.com");
        assertThatThrownBy(() -> answer.isNotWriter(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("답변의 작성자일 경우 답변을 삭제하고 삭제 이력을 반환한다.")
    void is_writer_delete_answer_return_delete_history() throws CannotDeleteException {
        DeleteHistory actual = answer.delete(writer);
        assertTrue(answer.isDeleted());
        assertThat(actual).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), writer));
    }
}
