package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {

    @Test
    @DisplayName("Answers contains 테스트")
    void contains() {
        Answers answers = getAnswers();

        Answer target = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");

        assertThat(answers.contains(target)).isTrue();
    }

    @Test
    @DisplayName("Answers add 테스트")
    void add() {
        Answers answers = Answers.init();

        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);

        assertThat(answers.contains(answer)).isTrue();
    }

    @Test
    @DisplayName("Answers remove 테스트")
    void remove() {
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "contents");
        Answers answers = Answers.from(Arrays.asList(answer1, answer2));
        answers.remove(answer2);

        Answer target = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "contents");

        assertAll(
                () -> assertThat(answers.contains(answer1)).isTrue(),
                () -> assertThat(answers.contains(target)).isFalse()
        );
    }

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Answers actual = getAnswers();
        Answers expected = getAnswers();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Answers actual = getAnswers();
        Answers expected = Answers.init();

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }

    private Answers getAnswers() {
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "contents");
        return Answers.from(Arrays.asList(answer1, answer2));
    }
}