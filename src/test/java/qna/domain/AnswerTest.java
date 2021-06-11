package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answers;

    @Test
    @DisplayName("Answer save test")
    void save() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        User answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        Answer expected = new Answer(answerUser, question, "Answers Contents1");

        // when
        Answer actual = answers.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        User answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        Answer answer = new Answer(answerUser, question, "Answers Contents1");

        answers.save(answer);

        // when
        List<Answer> actual = answers.findByQuestionAndDeletedFalse(question);

        // then
        assertAll(
                () -> assertThat(
                        actual.stream()
                        .filter(actualAnswer -> !actualAnswer.getQuestionId().equals(question.getId()))
                        .count())
                        .isEqualTo(0),
                () -> assertThat(
                        actual.stream()
                        .filter(actualAnswer -> actualAnswer.isDeleted())
                        .count())
                        .isEqualTo(0)
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        User answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        Answer answer = new Answer(answerUser, question, "Answers Contents1");

        answers.save(answer);

        // when
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(answer.getId());

        // then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get().getId()).isNotNull(),
                () -> assertThat(actual.get().getId()).isEqualTo(answer.getId()),
                () -> assertThat(actual.get().isDeleted()).isFalse()
        );

    }
}
