package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.content.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private TestEntityManager manager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user1", "password", "username1", "user1@test.com");
    }

    @Test
    @DisplayName("사용자를 저장한다")
    void save() {
        User actual = userRepository.save(user);

        assertThat(actual.getId()).isNotNull();
        assertEquals("user1", actual.getUserId());
    }

    @Test
    @DisplayName("UserId로 사용자를 검색한다")
    void findByUserId() {
        userRepository.save(user);
        User actual = userRepository.findByUserId("user1").get();

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("User entity의 동일성을 확인한다")
    void identity() {
        User userSaved = userRepository.save(user);
        User userFound = userRepository.findById(user.getId()).get();

        assertTrue(userSaved == userFound);
    }

    @Test
    @DisplayName("사용자를 수정한다(JPQL)")
    void update() {
        User userSaved = userRepository.save(user);
        userSaved.update(userSaved,
                new User("user1", "password", "changeName", "user1@test.com"));

        User userFound = userRepository.findByUserId("user1").get();

        assertAll(
                () -> assertThat(userFound).isNotNull(),
                () -> assertEquals("changeName", userFound.getName()),
                () -> assertTrue(userFound.getUpdatedAt().isAfter(userFound.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("사용자를 수정한다(flush)")
    void updateAndFlush() {
        User userSaved = userRepository.save(user);
        userSaved.update(userSaved,
                new User("user1", "password", "changeName", "user1@test.com"));

        userRepository.flush();
        assertAll(
                () -> assertEquals("changeName", userSaved.getName()),
                () -> assertTrue(userSaved.getUpdatedAt().isAfter(userSaved.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("사용자를 삭제한다")
    void delete() {
        User userSaved = userRepository.save(user);
        userRepository.delete(userSaved);
        userRepository.flush();

        Optional<User> userFound = userRepository.findById(user.getId());

        assertFalse(userFound.isPresent());
    }

    @Test
    @DisplayName("등록한 질문이 있는 사용자를 삭제한다")
    void deleteWithQuestion() {
        User userSaved = userRepository.save(user);
        Question question = questionRepository.save(
                new Question(userSaved, "test title", "test contents"));

        questionRepository.delete(question);
        userRepository.delete(userSaved);
        flushAndClear();
    }

    @Test
    @DisplayName("등록한 답변이 있는 사용자를 삭제한다")
    void deleteWithQuestionAndAnswer() {
        User userSaved = userRepository.save(user);
        Question question = questionRepository.save(
                new Question(userSaved, "test title", "test contents"));
        answerRepository.save(
                new Answer(userSaved, question, "test contents"));

        questionRepository.delete(question);
        userRepository.delete(userSaved);
        flushAndClear();
    }

    @Test
    @DisplayName("삭제 히스토리에 포함된 질문의 작성자인 사용자를 삭제한다")
    void deleteWithQuestionAndAnswerAndDeleteHistory() {
        User userSaved = userRepository.save(user);
        Question question = questionRepository.save(
                new Question(userSaved, "test title", "test contents"));
        DeleteHistory questionDeleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, question.getId(), userSaved));

        deleteHistoryRepository.delete(questionDeleteHistory);
        questionRepository.delete(question);
        userRepository.delete(user);
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}
