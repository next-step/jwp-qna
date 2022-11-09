package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {

    @Test
    @DisplayName("Answers 생성")
    void create() {
        //given
        Answers answers = new Answers();

        //expect
        assertThat(answers).isNotNull();
    }

}
