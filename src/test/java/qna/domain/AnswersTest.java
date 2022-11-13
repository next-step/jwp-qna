package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("답변 일급 콜렉션")
class AnswersTest {

    @DisplayName("답변을 추가한다.")
    @Test
    void add() {
        Answer answer = AnswerTest.ANSWER_1_ID;
        Answers answers = new Answers();
        answers.add(answer);
        assertAll(
                () -> assertThat(answers.getAnswers()).hasSize(1),
                () -> assertThat(answers.getAnswers()).containsExactly(answer)
        );
    }

    @DisplayName("질문자와 답변자가 다른 경우 답을 삭제할 수 없다.")
    @Test
    void delete_fail() {
        Answer answer = AnswerTest.ANSWER_1_ID;
        Answers answers = new Answers();
        answers.add(answer);
        assertThatThrownBy(() -> answers.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
