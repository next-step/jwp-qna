package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private DeleteHistory answerDeleteHistory;
    private DeleteHistory questionDeleteHistory;
    @BeforeEach
    void setup() {
        user = userRepository.save(UserTest.JAVAJIGI);
        answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, user, LocalDateTime.now());
        questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
    }

    @AfterEach
    void deleteAll() {
        deleteHistoryRepository.deleteAll();
    }

    @Test
    void save() {
        DeleteHistory actualQuestionDeleteHistory = deleteHistoryRepository.save(questionDeleteHistory);
        DeleteHistory actualAnswerDeleteHistory = deleteHistoryRepository.save(answerDeleteHistory);

        assertThat(actualQuestionDeleteHistory).isEqualTo(actualQuestionDeleteHistory);
        assertThat(answerDeleteHistory).isEqualTo(actualAnswerDeleteHistory);
    }

    @Test
    void findAllByContentType() {
        deleteHistoryRepository.save(questionDeleteHistory);
        deleteHistoryRepository.save(answerDeleteHistory);

        List<DeleteHistory> actual = deleteHistoryRepository.findAllByContentType(ContentType.ANSWER);

        assertThat(actual).contains(answerDeleteHistory);
    }

    @Test
    void findAllByUser() {
        deleteHistoryRepository.save(questionDeleteHistory);
        deleteHistoryRepository.save(answerDeleteHistory);

        List<DeleteHistory> actual = deleteHistoryRepository.findAllByUser(user);

        assertThat(actual).contains(questionDeleteHistory, answerDeleteHistory);
    }

    @Test
    void update() {
        DeleteHistory expected = deleteHistoryRepository.save(questionDeleteHistory);

        assertThat(expected.getContentType()).isEqualTo(ContentType.QUESTION);

        expected.setContentType(ContentType.ANSWER);
        DeleteHistory actual = deleteHistoryRepository.findById(expected.getId()).get();

        assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER);
    }

    @Test
    void delete() {
        DeleteHistory expected = deleteHistoryRepository.save(questionDeleteHistory);

        deleteHistoryRepository.delete(expected);

        assertThat(deleteHistoryRepository.findAll()).hasSize(0);
    }

}
