package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {
    private Answers answers;

    @BeforeEach
    void setUp() {
        //given
        answers = new Answers();
        Answer undeletedAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "answer contents");
        answers.add(undeletedAnswer);
    }

    @Test
    @DisplayName("답변 추가 후 사이즈 확인")
    void add() {
        //then
        assertThat(answers.getAnswers()).hasSize(1);
    }

    @Test
    @DisplayName("모든 답변 삭제 후 삭제기록 확인")
    void delete() throws CannotDeleteException {
        //when
        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI);

        //then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("삭제된 답변 존재할 경우 답변들 삭제 후 삭제기록 확인")
    void delete_with_deleted_answer() throws CannotDeleteException {
        //given
        Answer deletedAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "answer contents");
        deletedAnswer.delete(UserTest.JAVAJIGI);
        answers.add(deletedAnswer);

        //when
        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI);

        //then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    @DisplayName("삭제되지 않은 답변 사이즈 확인")
    void undeletedAnswers() throws CannotDeleteException {
        //given
        Answer deletedAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "answer contents");

        //when
        deletedAnswer.delete(UserTest.JAVAJIGI);
        answers.add(deletedAnswer);

        //then
        assertThat(answers.undeletedAnswers()).hasSize(1);
    }
}
