package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {
    @DisplayName("답변 추가")
    @Test
    void add() {
        Answers answers = new Answers();
        Answer answer = new Answer();

        answers.add(answer);

        assertThat(answers).isEqualTo(new Answers(Arrays.asList(answer)));
    }
}