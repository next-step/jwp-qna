package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
class AnswerRepositoryTest {
    private static Answer A1;
    private static Answer A2;

    private static User user1;
    private static User user2;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(UserTest.JAVAJIGI);
        user2 = userRepository.save(UserTest.SANJIGI);
        Question question = questionRepository.save(QuestionRepositoryTest.Q1.writeBy(user1));

        A1 = new Answer(user1, question, "Answers Contents1");
        A2 = new Answer(user2, question, "Answers Contents2");
    }

    @Test
    @DisplayName("Answer 도메인 생성 테스트")
    void generate01() {
        // given && when
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        em.flush();
        em.clear();

        // then
        Optional<Answer> findA1 = answerRepository.findById(a1.id());
        Optional<Answer> findA2 = answerRepository.findById(a2.id());

        assertAll(
            () -> assertTrue(findA1.isPresent()),
            () -> assertEquals(a1, findA1.get()),
            () -> assertTrue(findA2.isPresent())
        );
    }

    @Test
    @DisplayName("AnswerId를 기준으로 삭제되지 않은 Answer 도메인을 조회한다.")
    void find01() throws CannotDeleteException {
        // given && when
        A1.toDeleted(user1);
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        em.flush();
        em.clear();

        // then
        Optional<Answer> findA1 = answerRepository.findByIdAndDeletedFalse(a1.id());
        Optional<Answer> findA2 = answerRepository.findByIdAndDeletedFalse(a2.id());

        assertAll(
            () -> assertFalse(findA1.isPresent()),
            () -> assertTrue(findA2.isPresent()),
            () -> assertEquals(a2, findA2.get())
        );
    }

    @Test
    @DisplayName("QuestionId를 기준으로 삭제되지 않은 Answer를 조회한다.")
    void find02() throws CannotDeleteException {
        // given && when
        A1.toDeleted(user1);
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        em.flush();
        em.clear();

        // then
        List<Answer> answersByQuestionId = answerRepository.findByQuestionIdAndDeletedFalse(
            a1.question().id());

        assertAll(
            () -> assertThat(answersByQuestionId).hasSize(1),
            () -> assertThat(answersByQuestionId).contains(a2)
        );
    }
}
