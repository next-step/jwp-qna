package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    @DisplayName("Answers일급 콜렉션 객체 생성")
    @Test
    void create() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

        assertThat(answers.size()).isEqualTo(2);
    }
}
