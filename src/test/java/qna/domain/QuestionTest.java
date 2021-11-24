package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents3").writeBy(UserTest.SANJIGI);
    public static final Question Q4 = new Question("title4", "contents4").writeBy(UserTest.SANJIGI);
    public static final Question Q5 = new Question("title5", "contents5").writeBy(UserTest.HONGHEE);

    @DisplayName("정상적으로 삭제가 되는 경우 테스트 - Answer이 존재하지 않음")
    @Test
    public void deleteQuestionTest() throws CannotDeleteException {

        // when
        User loginUser = UserTest.HONGHEE;
        Question question = QuestionTest.Q5;

        // then
        List<DeleteHistory> deleteHistories = question.deleteQuestion(loginUser);

        // given
        Assertions.assertThat(deleteHistories.size()).isEqualTo(1);
        Assertions.assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("정상적으로 삭제가 되는 경우 테스트 - Answer이 존재(작성자가 같은 경우)")
    @Test
    public void deleteQuestionWithAnswerTest() throws CannotDeleteException {

        // when
        User loginUser = UserTest.HONGHEE;
        Question question = QuestionTest.Q5;
        Answer answer = AnswerTest.A5;
        question.addAnswer(answer);

        // then
        List<DeleteHistory> deleteHistories = question.deleteQuestion(loginUser);

        // given
        Assertions.assertThat(deleteHistories.size()).isEqualTo(2);
        Assertions.assertThat(question.isDeleted()).isTrue();
        Assertions.assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("정상적으로 삭제가 되지 않는 경우 테스트 - Answer이 존재하지 않음")
    @Test
    public void deleteQuestionFailureTest() {

        // when
        User loginUser = UserTest.HONGHEE;
        Question question = QuestionTest.Q1;

        // then
        Assertions.assertThatThrownBy(() -> {
            List<DeleteHistory> deleteHistories = question.deleteQuestion(loginUser);
            Assertions.assertThat(deleteHistories).isNull();
        }).isInstanceOf(CannotDeleteException.class);

        // given
        Assertions.assertThat(question.isDeleted()).isFalse();
    }

    @DisplayName("정상적으로 삭제가 되지 않는 경우 테스트 - Answer이 존재(작성자가 다른 경우)")
    @Test
    public void deleteQuestionWithAnswerFailureTest() {

        // when
        User loginUser = UserTest.SANJIGI;
        Question question = QuestionTest.Q3;
        Answer answer = AnswerTest.A1;
        question.addAnswer(answer);

        // then
        Assertions.assertThatThrownBy(() -> {
            List<DeleteHistory> deleteHistories = question.deleteQuestion(loginUser);
            Assertions.assertThat(deleteHistories).isNull();
        }).isInstanceOf(CannotDeleteException.class);

        // given
        Assertions.assertThat(question.isDeleted()).isFalse();
        Assertions.assertThat(answer.isDeleted()).isFalse();
    }
}
