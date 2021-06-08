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
public class AnswerTest {

    @Autowired
    private AnswerRepository answers;

    private Answer A1;
    private Answer A2;
    private Question question1;
    private Question question2;
    private User writer1;
    private User writer2;

    @BeforeEach
    void setUp() {
        question1 = new Question("Question1 title", "Question1 contents");
        question2 = new Question("Question2 title", "Question2 contents");

        writer1 = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        writer2 = new User("user2", "user2Pass", "User2", "user2@gmail.com");

        A1 = new Answer(
                writer1, question1, "Answers Contents1");
        A2 = new Answer(
                writer2, question2, "Answers Contents2");

    }

    @Test
    @DisplayName("Answer save test")
    void save() {
        assertThat(answers.save(A1)).isEqualTo(A1);
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(A1);
        List<Answer> actual = answers.findByQuestionAndDeletedFalse(question1);
        assertAll(
                () -> assertThat(actual.get(0).getQuestionId()).isEqualTo(question1.getId()),
                () -> assertThat(actual.get(0).isDeleted()).isFalse()
        );

    }

    @Test
    void findByIdAndDeletedFalse() {
        answers.save(A2);
        Optional<Answer> actual = answers.findByIdAndDeletedFalse(A2.getId());
        assertAll(
                () -> assertThat(actual.get().getId()).isNotNull(),
                () -> assertThat(actual.get().getId()).isEqualTo(A2.getId()),
                () -> assertThat(actual.get().isDeleted()).isFalse()
        );

    }
}
