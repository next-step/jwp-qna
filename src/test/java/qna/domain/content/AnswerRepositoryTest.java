package qna.domain.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.domain.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("AnswerRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                new User("user1", "password", "user1", "user1@test.com"));
        question = questionRepository.save(new Question(user, "title", "contents"));
        answer = new Answer(user, question, "contents");
    }

    @Test
    @DisplayName("답변을 저장한다")
    void save() {
        Answer actual = answerRepository.save(answer);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(user, actual.getWriter()),
                () -> assertEquals(question, actual.getQuestion())
        );
    }

    @Test
    @DisplayName("입력된 질문의 deleted가 false인 답변을 검색한다")
    void findByQuestionAndDeletedFalse() {
        answerRepository.save(answer);

        List<Answer> answersFound =
                answerRepository.findByQuestionAndDeletedFalse(question);

        assertAll(
                () -> assertThat(answersFound).hasSize(1),
                () -> assertTrue(answersFound.stream().noneMatch(Answer::isDeleted))
        );
    }

    @Test
    @DisplayName("입력된 id의 deleted가 false인 답변을 검색한다")
    void findByIdAndDeletedFalse() {
        Answer answerSaved = answerRepository.save(answer);
        Answer answerFound = answerRepository.findByIdAndDeletedFalse(answerSaved.getId()).get();

        assertFalse(answerFound.isDeleted());
    }

    @Test
    @DisplayName("Answer entity의 동일성을 확인한다")
    void identity() {
        final Answer answerSaved = answerRepository.save(answer);
        final Answer answerFound = answerRepository.findById(answerSaved.getId()).get();

        assertTrue(answerSaved == answerFound);
    }

    @Test
    @DisplayName("답변을 수정한다(JPQL)")
    void update() {
        Answer answerSaved = answerRepository.save(answer);
        answerSaved.update(user, "change contents");

        Answer answerFound = answerRepository.findByIdAndDeletedFalse(answerSaved.getId()).get();

        assertAll(
                () -> assertThat(answerFound).isNotNull(),
                () -> assertEquals("change contents", answerFound.getContents()),
                () -> assertTrue(answerFound.getUpdatedAt().isAfter(answerFound.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("답변을 수정한다(flush)")
    void updateAndFlush() {
        Answer answerSaved = answerRepository.save(answer);
        answerSaved.update(user,"change contents");

        answerRepository.flush();
        assertAll(
                () -> assertEquals("change contents", answerSaved.getContents()),
                () -> assertTrue(answerSaved.getUpdatedAt().isAfter(answerSaved.getCreatedAt()))
        );
    }

    @Test
    @DisplayName("답변을 삭제한다")
    void delete() {
        Answer answerSaved = answerRepository.save(answer);
        answerRepository.delete(answerSaved);
        answerRepository.flush();

        Optional<Answer> answerFound = answerRepository.findById(answerSaved.getId());

        assertFalse(answerFound.isPresent());
    }
}
