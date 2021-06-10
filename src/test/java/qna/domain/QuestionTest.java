package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {

    @Autowired
    QuestionRepository questions;

    @Test
    @DisplayName("Question save test")
    void save() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question expected = new Question("Question1 title", "Question1 contents").writeBy(questionUser);

        // when
        Question actual = questions.save(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByDeletedFalse() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);

        questions.save(question);

        // when
        List<Question> actual = questions.findByDeletedFalse();

        // then
        assertAll(
                () -> assertThat(
                        actual.stream()
                                .filter(actualQuestion -> actualQuestion.isDeleted())
                                .count())
                        .isEqualTo(0)
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);

        questions.save(question);

        // when
        Optional<Question> actual = questions.findByIdAndDeletedFalse(question.getId());

        // then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get()).isEqualTo(question),
                () -> assertThat(actual.get().isDeleted()).isFalse()
        );
    }
}
