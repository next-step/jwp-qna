package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private DeleteHistory deleteHistory;
    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(UserTest.LEWISSEO);
        question = questionRepository.save(new Question("title1", "contents1"));
        deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now()));
    }

    @DisplayName("deleteHistory 삭제")
    @Test
    void removeDeleteHistoryTest() {
        assertThat(deleteHistoryRepository.findAll().size()).isEqualTo(1);
        deleteHistoryRepository.delete(deleteHistory);
        assertThat(deleteHistoryRepository.findAll().size()).isZero();
    }
}
