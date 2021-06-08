package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistory;

    private Question question1;
    private User writer1;
    private Answer answer1;

    @BeforeEach
    void setUp() {
        writer1 = new User("user1", "user1Pass", "User1", "user1@gmail.com");

        question1 = new Question("Question1 title", "Question1 contents").writeBy(writer1);

        answer1 = new Answer(writer1, question1, "Answers Contents1");
    }

    @Test
    void saveDeletedQuestion() {
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question1.getId(), writer1, LocalDateTime.now());
        DeleteHistory actual = deleteHistory.save(expected);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isSameAs(expected)
        );
    }

    @Test
    void saveDeletedAnswer() {
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer1.getId(), writer1, LocalDateTime.now());
        DeleteHistory actual = deleteHistory.save(expected);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isSameAs(expected)
        );
    }
}