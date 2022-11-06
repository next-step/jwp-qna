package qna.domain.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.User;
import qna.domain.UserRepository;
import qna.domain.content.Answer;
import qna.domain.content.AnswerRepository;
import qna.domain.content.Question;
import qna.domain.content.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QuestionRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager manager;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("user1", "password", "user1", "user1@test.com"));
        question = new Question(user, "title", "contents");
    }

    @Test
    @DisplayName("질문을 저장한다")
    void save() {
        Question actual = questionRepository.save(question);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(user, actual.getWriter())
        );
    }

    @Test
    @DisplayName("deleted가 false인 질문들을 검색한다")
    void findByDeletedFalse() {
        questionRepository.save(new Question(user, "title", "contents"));
        questionRepository.save(new Question(user, "title2", "contents2"));

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).hasSize(2),
                () -> assertTrue(questions.stream().noneMatch(Question::isDeleted))
        );
    }

    @Test
    @DisplayName("입력된 id의 deleted가 false인 질문을 검색한다")
    void findByIdDeletedFalse() {
        Question questionSaved = questionRepository.save(question);
        Question questionFound = questionRepository.findByIdAndDeletedFalse(questionSaved.getId()).get();

        assertFalse(questionFound.isDeleted());
    }

    @Test
    @DisplayName("Question entity의 동일성을 확인한다")
    void identity() {
        Question questionSaved = questionRepository.save(question);
        Question questionFound = questionRepository.findById(questionSaved.getId()).get();

        assertTrue(questionSaved == questionFound);
    }

    @Test
    @DisplayName("질문을 수정한다(JPQL)")
    void update() {
        Question questionSaved = questionRepository.save(question);
        questionSaved.update(user, "change title", "change contents");

        Question questionFound =
                questionRepository.findByIdAndDeletedFalse(questionSaved.getId()).get();

        assertAll(
                () -> assertThat(questionFound).isNotNull(),
                () -> assertEquals("change title", questionFound.getTitle()),
                () -> assertEquals("change contents", questionFound.getContents()),
                () -> assertTrue(questionFound.getUpdatedAt().isAfter(questionFound.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("질문을 수정한다(flush)")
    void updateAndFlush() {
        Question questionSaved = questionRepository.save(question);
        questionSaved.update(user, "change title", "change contents");

        questionRepository.flush();
        assertAll(
                () -> assertEquals("change title", questionSaved.getTitle()),
                () -> assertEquals("change contents", questionSaved.getContents()),
                () -> assertTrue(questionSaved.getUpdatedAt().isAfter(questionSaved.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("질문을 삭제한다")
    void delete() {
        Question questionSaved = questionRepository.save(question);
        questionRepository.delete(questionSaved);
        questionRepository.flush();

        Optional<Question> questionFound = questionRepository.findById(questionSaved.getId());

        assertFalse(questionFound.isPresent());
    }

    @Test
    @DisplayName("답변이 있는 질문을 삭제한다")
    void deleteWithAnswer() {
        Question questionSaved = questionRepository.save(question);
        answerRepository.save(new Answer(user, questionSaved, "contents"));

        questionRepository.delete(questionSaved);
        flushAndClear();
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }
}
