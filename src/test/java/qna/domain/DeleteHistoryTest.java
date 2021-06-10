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

    @Test
    void saveDeletedQuestion() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), questionUser, LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistory.save(expected);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isSameAs(expected)
        );
    }

    @Test
    void saveDeletedAnswer() {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        User answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        Answer answer = new Answer(answerUser, question, "Answers Contents1");
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, answer.getId(), answerUser, LocalDateTime.now());

        // when
        DeleteHistory actual = deleteHistory.save(expected);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isSameAs(expected)
        );
    }
}