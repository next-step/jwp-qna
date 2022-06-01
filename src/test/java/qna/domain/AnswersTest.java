package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswersTest {

    @Test
    @DisplayName("다른 사람의 답변이 있으면 삭제시 CannotDeleteException 예외가 발생해야 한다.")
    void cannotDeleteTest() {
        // given
        List<Answer> answerList = Arrays.asList(AnswerTest.A1, AnswerTest.A2);
        Answers answers = new Answers(answerList);

        // then
        assertThatThrownBy(() -> answers.delete(UserTest.JAVAJIGI))
            .isExactlyInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변이 삭제되면 DeleteHistories가 반환되어야 한다.")
    void deleteAnswersTest() throws CannotDeleteException {
        // given
        Answer answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents1 - answer");
        Answer answer2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents2 - answer");
        Answers savedAnswers = new Answers();
        savedAnswers.add(answer1);
        savedAnswers.add(answer2);

        // when
        DeleteHistories deleteHistories = savedAnswers.delete(UserTest.JAVAJIGI);

        // then
        assertThat(deleteHistories).isNotNull();
    }

}
