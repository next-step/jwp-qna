package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void testDeleteAll() throws CannotDeleteException {
        User writer = new User("userId", "password", "name", "email");
        Question question = Question.of(1L, "title", "contents", writer);
        Answer answer1 = Answer.of(1L, writer, question, "");
        Answer answer2 = Answer.of(2L, writer, question, "");
        answers = new Answers(Arrays.asList(answer1, answer2));
        answers.deleteAll(writer);
        assertThat(answers.getAnswers()).allMatch(Answer::isDeleted);
        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    void GivenHasAnoterWriterAnswersWhenDeleteAllThenThrowExceptionAndDoNotChanged() {
        User questionWriter = new User("userId1", "password", "name1", "email1");
        User otherWriter = new User("userId2", "password", "name2", "email2");
        Question question = Question.of(1L, "title", "contents", questionWriter);
        Answer answer1 = Answer.of(1L, questionWriter, question, "");
        Answer answer2 = Answer.of(2L, otherWriter, question, "");
        answers = new Answers(Arrays.asList(answer1, answer2));
        assertThatThrownBy(() -> answers.deleteAll(questionWriter))
                .isInstanceOf(CannotDeleteException.class);
        assertThat(answers.getDeletedAnswers()).hasSize(0);
    }
}
