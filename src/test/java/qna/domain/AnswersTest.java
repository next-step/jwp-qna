package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    public static final Answers answers = new Answers(Collections.singletonList(AnswerTest.A1));
    public static final Answers answers2 = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

    @Test
    @DisplayName("deleteAnswers의 결과 삭제 히스토리가 반환된다")
    void deleteAnswers() {
        // given
        final Answer answer = new Answer(3L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents3");
        final Answers answers = new Answers();
        answers.addAnswer(answer);

        // when
        List<DeleteHistory> actual = answers.deleteAnswers(UserTest.JAVAJIGI);

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("deleteAnswers의 결과 삭제하려는 답변 중 한 개라도 작성자와 불일치하면 삭제가 되지 않는다")
    void deleteAnswersException() {
        // given & when & then
        assertThatThrownBy(() -> answers.deleteAnswers(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("nonDeletedAnswers의 결과 삭제가 되지 않은 답변 리스트가 반환된다")
    void getAnswers() {
        // given & when
        List<Answer> answerList = answers2.nonDeletedAnswers();

        // then
        assertThat(answerList).hasSize(2);
    }
}
