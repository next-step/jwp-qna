package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Answer_List_일급객체_테스트")
public class AnswersTest {
    private Answer answer;

    @BeforeEach
    void before() {
        answer = AnswerTest.A1;
    }

    @DisplayName("Answers_add_테스트")
    @Test
    void add() {
        Answers answers = new Answers();
        answers.add(answer);
        assertThat(answers.getAnswers()).containsExactly(answer);
    }
}
