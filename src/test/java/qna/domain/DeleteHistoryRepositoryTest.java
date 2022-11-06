package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.content.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeleteHistoryRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private User user;
    private Question question;
    private DeleteHistory questionDeleteHistory;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("user1", "password", "user1", "user1@test.com"));
        question = questionRepository.save(new Question(user, "title", "contents"));
        questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), user);
    }

    @Test
    @DisplayName("삭제 이력을 저장한다")
    void save() {
        DeleteHistory actual = deleteHistoryRepository.save(questionDeleteHistory);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(ContentType.QUESTION, actual.getContentType()),
                () -> assertEquals(question.getId(), actual.getContentId()),
                () -> assertEquals(user, actual.getDeletedBy())
        );
    }

    @Test
    @DisplayName("DeleteHistory entity의 동일성을 확인한다")
    void identity() {
        DeleteHistory deleteHistorySaved = deleteHistoryRepository.save(questionDeleteHistory);
        DeleteHistory deleteHistoryFound = deleteHistoryRepository
                .findById(deleteHistorySaved.getId()).get();

        assertTrue(deleteHistorySaved == deleteHistoryFound);
    }

    @Test
    @DisplayName("삭제 이력을 삭제한다")
    void delete() {
        DeleteHistory deleteHistorySaved = deleteHistoryRepository.save(questionDeleteHistory);
        deleteHistoryRepository.delete(deleteHistorySaved);
        deleteHistoryRepository.flush();

        Optional<DeleteHistory> deleteHistoryFound = deleteHistoryRepository
                .findById(deleteHistorySaved.getId());

        assertFalse(deleteHistoryFound.isPresent());
    }
}
