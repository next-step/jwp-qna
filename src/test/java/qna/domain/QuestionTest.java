package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 동일한지 테스트")
    void is_owner_check() {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        assertTrue(question.isOwner(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("질문을 삭제")
    void question_delete() throws CannotDeleteException {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        question.delete(UserTest.JAVAJIGI);
        assertTrue(question.isDeleted());
    }

    @Test
    @DisplayName("질문 삭제 시 작성자가 제거하려는 사용자와 달라서 삭제 불 가능  CannotDeleteException 발생")
    void question_delete_exception_cannot_delete_exception() throws CannotDeleteException {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 삭제 후 반환 된 DeleteHistories 테스트")
    void question_delete_return_deleteHistories() throws CannotDeleteException {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        DeleteHistories deleteHistories = question.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories.deleteHistoryCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문 삭제 시 답변까지 삭제 되어 2개의 히스토리 내용을 갖는 테스트")
    void question_delete_and_answer_return_deleteHistories() throws CannotDeleteException {
        Question question = new Question("test", "test");
        question.writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "contents");
        question.addAnswer(answer);

        DeleteHistories deleteHistories = question.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories.deleteHistoryCount()).isEqualTo(2);
    }
}
