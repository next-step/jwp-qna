package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {

    @DisplayName("Answers에 Answer을 추가할 수 있다.")
    @Test
    void add() {
        Answers answers = new Answers();
        Answer answer = new Answer();

        answers.add(answer);

        Assertions.assertThat(answers.contains(answer)).isTrue();
    }

    @DisplayName("Answers에 Answer을 제거할 수 있다.")
    @Test
    void remove() {
        Answers answers = new Answers();
        Answer answer = new Answer();
        answers.add(answer);

        answers.remove(answer);

        Assertions.assertThat(answers.isEmpty()).isTrue();
    }
}
