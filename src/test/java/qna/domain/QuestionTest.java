package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question의 작성자를 변경할 수 있다.")
    @Test
    void write_by() {
        Question actual = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        actual.writeBy(UserTest.SANJIGI);

        assertThat(actual.isOwner(UserTest.SANJIGI)).isTrue();
    }

    @DisplayName("delete를 수행하면 삭제상태가 true로 변경된다.")
    @Test
    void delete() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.SANJIGI);

        question.delete(UserTest.SANJIGI);

        assertThat(question.isDeleted()).isTrue();
    }


    @DisplayName("작성자와 동일하지 않은 사용자가 삭제하면 CannotDeleteException 이 발생한다.")
    @Test
    void delete_exception() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.SANJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("답변이 있는 질문의 모든 답변과 질문의 작성자가 로그인유저와 동일하면 삭제가 가능하다.")
    @Test
    void delete_answer() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.SANJIGI);
        Answer answer = new Answer(1L, UserTest.SANJIGI, question, "content");

        DeleteHistories delete = question.delete(UserTest.SANJIGI);

        assertThat(delete.getUnmodifiableDeleteHistories()).containsExactly(
                DeleteHistory.of(ContentType.ANSWER, 1L, UserTest.SANJIGI),
                DeleteHistory.of(ContentType.QUESTION, 1L, UserTest.SANJIGI));

    }

    @DisplayName("답변이 있는 질문의 모든 답변과 질문의 작성자가 로그인유저와 동일하지 않으면 CannotDeleteException 이 발생한다.")
    @Test
    void delete_answer_exception() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.SANJIGI);
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "content");

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);

    }
}
