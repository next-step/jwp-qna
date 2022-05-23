package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    private Answers answers;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setup() {
        question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "contents");
        answers = new Answers();
    }

    @Test
    void add_성공() {
        answers.add(question.getId(), answer);

        assertThat(answers.getAnswers()).containsExactly(answer);
    }

    @ParameterizedTest
    @NullSource
    void add_answer_null(Answer nullAnswer) {
        assertThatThrownBy(() -> answers.add(question.getId(), nullAnswer))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_answer_other_question() {
        assertThatThrownBy(() -> answers.add(2L, answer))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_answer_duplicate() {
        answers.add(question.getId(), answer);

        assertThatThrownBy(() -> answers.add(question.getId(), answer))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void delete_성공() {
        answers.add(question.getId(), answer);
        answers.delete(question.getWriter());

        List<Answer> answers = this.answers.getAnswers();

        for (Answer answer : answers) {
            assertThat(answer.isDeleted()).isTrue();
        }
    }
}