package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.CannotUpdateException;

public class QuestionTest {

    private User u1;
    private User u2;
    private Question q1;
    private Question q2;
    private Answer a1;
    private Answer a2;

    @BeforeEach
    void setup() {
        u1 = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        u2 = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        q1 = new Question(1L, "title1", "contents1").writeBy(u1);
        q2 = new Question(2L, "title2", "contents2").writeBy(u2);
        a1 = new Answer(1L, u1, q1, "contents");
        a2 = new Answer(2L, u2, q1, "contents");
    }

    @DisplayName("질문의 작성자를 변경할수 있다")
    @Test
    void writeBy_test() {
        Question question = new Question("title1", "contents1").writeBy(u1);

        question.writeBy(u2);

        assertEquals(question.getWriter(), u2);
    }

    @DisplayName("질문의 작성자를 확인할 수 있다")
    @Test
    void isOwner_test() {
        Question question = new Question("title1", "contents1").writeBy(u1);
        assertTrue(question.isOwner(u1));
    }

    @DisplayName("질문의 중복되지 않은 답변을 추가할 수 있다.")
    @Test
    void addAnswer_test() {
        assertFalse(q1.getAnswers().contains(a1));
        q1.addAnswer(a1);

        assertTrue(q1.getAnswers().contains(a1));
    }

    @DisplayName("답변의 질문을 잘못된 질문에 추가하려고 하면 CannotUpdateException 에러를 발생시킨다.")
    @Test
    void addAnswer_to_wrong_question_exception() {

        assertThat(a1.getQuestion()).isNotEqualTo(q2);
        assertThatThrownBy(() -> q2.addAnswer(a1))
                .isInstanceOf(CannotUpdateException.class);
    }

    @DisplayName("삭제되지 않은 질문을 삭제하면 DeleteHistories가 리턴 된다.")
    @Test
    void delete_non_deleted_question_test() throws CannotDeleteException {
        q1.addAnswer(a1);
        assertFalse(q1.isDeleted());
        DeleteHistories deleteHistories = q1.delete(u1);
        assertAll(
                () -> assertEquals(2, deleteHistories.getSize()),
                () -> assertTrue(q1.isDeleted()),
                () -> assertTrue(a1.isDeleted())
        );


    }

    @DisplayName("삭제할때 로그인 유저와 id가 다르면 CannotDeleteException을 발생시킨다.")
    @Test
    void delete_non_deleted_question_exception() {

        q1.addAnswer(a1);

        assertThatThrownBy(() -> q1.delete(u2))
                .isInstanceOf(CannotDeleteException.class);

    }
}
