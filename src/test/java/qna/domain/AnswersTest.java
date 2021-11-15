package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswersTest {
    private Answer answer;
    private Answers answers;

    @BeforeEach
    void setUp() {
        User writer = new User("userId", "password", "name", "email");
        Question question = Question.of(1L, "title", "contents", writer);
        answer = Answer.of(1L, writer, question, "");
        answers = new Answers();
    }

    @Test
    void testAdd() {
        answers.add(answer);
        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    void testContains() {
        answers.add(answer);
        assertThat(answers.contains(answer)).isTrue();
    }

    @Test
    void testRemove() {
        answers = new Answers(Arrays.asList(answer));
        answers.remove(answer);
        assertThat(answers.size()).isEqualTo(0);
    }
}
