package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    @Test
    @DisplayName("모든 답변에 대해 삭제 상태로 변경 할 수 있다.")
    void change_all_delete() throws CannotDeleteException {
        Answer answerOne = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer answerTwo = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answers answers = new Answers(Arrays.asList(answerOne, answerTwo));
        answers.deleteAll(UserTest.JAVAJIGI);
        assertThat(answerOne.isDeleted()).isTrue();
        assertThat(answerTwo.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자가 달라서 삭제를 하지 못하고 CannotDeleteException 발생")
    void change_all_delete_throw_cannot_delete_exception() {
        Answer answerOne = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer answerTwo = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "contents");
        Answers answers = new Answers(Arrays.asList(answerOne, answerTwo));
        assertThatThrownBy(() -> answers.deleteAll(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("모든 답변에 대해 삭제 후 반환 받은 List<DeleteHisotry> 개수 확인")
    void change_all_delete_retrun_delete() throws CannotDeleteException {
        Answer answerOne = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer answerTwo = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answers answers = new Answers(Arrays.asList(answerOne, answerTwo));
        DeleteHistories deleteHistories = answers.deleteAll(UserTest.JAVAJIGI);
        assertThat(deleteHistories.deleteHistoryCount()).isEqualTo(2);
    }
}
