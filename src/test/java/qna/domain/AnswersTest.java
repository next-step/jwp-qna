package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.fixture.TestFixture.*;

class AnswersTest {

    private Answer answer;

    @BeforeEach
    void setup() {
        answer = new Answer(1L, JAVAJIGI, Q1, "answer_contents");
    }

    @Test
    void add() {
        Answers answers = new Answers();

        answers.add(answer);

        assertThat(answers.getAnswers()).contains(answer);
    }

    @Test
    void deleteAll() {
        Answers answers = new Answers();
        answers.add(answer);

        DeleteHistories deleteHistories = answers.deleteAll();

        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

    @Test
    void isIncludedFromOtherThan() {
        Answers answers = new Answers();
        answers.add(answer);

        assertThat(answers.isIncludedFromOtherThan(SANJIGI)).isTrue();
    }
}
