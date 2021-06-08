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

    private Question question1;
    private Question question2;
    private User writer1;
    private User writer2;

    @BeforeEach
    void setUp() {
        writer1 = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        writer2 = new User("user2", "user2Pass", "User2", "user2@gmail.com");

        question1 = new Question("Question1 title", "Question1 contents").writeBy(writer1);
        question2 = new Question("Question2 title", "Question2 contents").writeBy(writer2);

        questions.save(question1);
        questions.save(question2);
    }

    @Test
    @DisplayName("Question save test")
    void save() {
        assertThat(questions.save(question1)).isEqualTo(question1);
    }

    @Test
    void findByDeletedFalse() {

        List<Question> actual = questions.findByDeletedFalse();
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.stream()
                        .filter(question -> question.isDeleted())
                        .count())
                        .isEqualTo(0)
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Question> actual = questions.findByIdAndDeletedFalse(question1.getId());
        assertThat(actual.get()).isSameAs(question1);
    }

}
