package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswersTest {

    @Test
    void create() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1,
                                                    AnswerTest.A2));

        assertThat(answers.answers()).contains(AnswerTest.A1, AnswerTest.A2);
    }

    @Test
    void deleteAll_Test() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1,
                                                    AnswerTest.A2));

        answers.deleteAllHistory();

        assertThat(answers.answers().stream()
                                    .anyMatch(answer -> !answer.isDeleted())
        ).isFalse();
    }
}
