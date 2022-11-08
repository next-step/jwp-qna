package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

public class AnswerTest {


    private User u1;
    private User u2;
    private Question q1;
    private Question q2;
    private Answer a1;

    @BeforeEach
    void setup() {
        u1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        u2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        q1 = new Question(1L, "title1", "contents1").writeBy(u1);
        q2 = new Question(2L, "title1", "contents1").writeBy(u2);
        a1 = new Answer(1L, u1, q1, "contents");
    }


    @DisplayName("답변 생성시 작성자가 없으면 UnAuthorizedException 발생시킨다")
    @Test
    void createAnswer_withoutWriter_exception() {
        assertThatThrownBy(() -> new Answer(null, q1, "contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("답변 생성시 질문이 없으면 NotFoundException 발생시킨다")
    @Test
    void createAnswer_withoutQuestion_exception() {
        assertThatThrownBy(() -> new Answer(u1, null, "contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("답변의 작성자가 주어진 작성자가 맞는지 확인할 수 있다")
    @Test
    void isOwner_test() {
        Answer answer = new Answer(u1, q1, "contents");
        assertAll(
                () -> assertTrue(answer.isOwner(u1)),
                () -> assertFalse(answer.isOwner(u2))
        );
    }

    @DisplayName("답변에 해당하는 질문을 변경할 수 있다")
    @Test
    void toQuestion_test() {
        Answer answer = new Answer(u1, q1, "contents");
        answer.toQuestion(q2);
        assertThat(answer.getQuestion()).isEqualTo(q2);
    }

    @DisplayName("삭제되지 않은 답변을 삭제하면 DeleteHistory가 리턴 된다.")
    @Test
    void delete_non_deleted_answer_test() throws CannotDeleteException {

        assertFalse(a1.isDeleted());
        DeleteHistory deleteHistory = a1.delete(u1);
        assertNotNull(deleteHistory);
    }

    @DisplayName("삭제된 답변을 또 삭제하려고 하면 이미 CannotDeleteException")
    @Test
    void delete_answer_already_deleted_test() throws CannotDeleteException {

        assertFalse(a1.isDeleted());
        DeleteHistory deleteHistory = a1.delete(u1);
        assertNotNull(deleteHistory);
        assertThatThrownBy(() -> a1.delete(u1))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("삭제할때 로그인 유저와 id가 다르면 CannotDeleteException을 발생시킨다.")
    @Test
    void delete_answer_wrong_user_exception() {

        assertThatThrownBy(() -> a1.delete(u2))
                .isInstanceOf(CannotDeleteException.class);

    }
}
