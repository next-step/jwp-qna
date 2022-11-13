package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("답변 일급 콜렉션")
class AnswersTest {

    @DisplayName("답변을 추가한다.")
    @Test
    void name() {
        Answer answer = AnswerTest.ANSWER_1_ID;
        Answers answers = new Answers();
        answers.add(answer);
        assertAll(
                () -> assertThat(answers.getAnswers()).hasSize(1),
                () -> assertThat(answers.getAnswers()).containsExactly(answer)
        );
    }
}
