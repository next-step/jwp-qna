package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AnswersTest {

    Answers answers = new Answers();

    @Test
    void addTest() {
        answers.add(AnswerTest.A1);
        assertThat(answers.get(0)).isEqualTo(AnswerTest.A1);
    }

    @Test
    void deleteAnswer() throws CannotDeleteException {
        answers.add(AnswerTest.A1);

        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(1);

    }
}