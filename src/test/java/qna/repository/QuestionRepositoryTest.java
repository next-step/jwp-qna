package qna.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager manager;
    private User u1;
    private User u2;
    private Question q1;
    private Question q2;

    @BeforeEach
    void setup() {
        u1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        u2 = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        q1 = new Question("title1", "contents1").writeBy(u1);
        q2 = new Question("title2", "contents2").writeBy(u2);

    }

    @DisplayName("질문과 답변 세트를 저장할 수 있다.")
    @Test
    void save_test() {
        Answer a1 = new Answer(u1, q1, "Answers");
        q1.getAnswers().add(a1);
        Question question = questionRepository.save(q1);
        assertAll(
                () -> assertNotNull(question),
                () -> assertEquals(1, answerRepository.findAll().size())
        );

    }

    @DisplayName("질문과 답변 세트를 조회할 수 있다.")
    @Test
    void find_test() {
        Answer a1 = new Answer(u1, q1, "Answers");
        q1.getAnswers().add(a1);
        Question question = questionRepository.save(q1);
        manager.flush();
        manager.clear();
        Optional<Question> actual = questionRepository.findById(question.getId());
        assertAll(
                () -> assertTrue(actual.isPresent()),
                () -> assertNotNull(actual.get().getAnswers().get(0).getId())
        );

    }

    @DisplayName("삭제되지 않은 답변을 전부 조회할 수 있다")
    @Test
    void findByDeletedFalse_test() {

        questionRepository.save(q1);
        questionRepository.save(q2);

        List<Question> beforeDeleted = questionRepository.findByDeletedFalse();

        q1.setDeleted(true);

        List<Question> afterDeleted = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertTrue(q1.isDeleted()),
                () -> assertFalse(q2.isDeleted()),
                () -> assertEquals(beforeDeleted.size(), 2),
                () -> assertEquals(afterDeleted.size(), 1),
                () -> assertFalse(afterDeleted.get(0).isDeleted())
        );

    }

    @DisplayName("삭제되지 않은 답변을 아이디로 조회할 수 있다.")
    @Test
    void findByIdAndDeletedFalse_test() {
        Question question1 = questionRepository.save(q1);
        Question question2 = questionRepository.save(q2);

        q1.setDeleted(true);

        Optional<Question> expect1 = questionRepository.findByIdAndDeletedFalse(question1.getId());
        Optional<Question> expect2 = questionRepository.findByIdAndDeletedFalse(question2.getId());

        assertAll(
                () -> assertTrue(q1.isDeleted()),
                () -> assertFalse(q2.isDeleted()),
                () -> assertFalse(expect1.isPresent()),
                () -> assertTrue(expect2.isPresent())
        );
    }

    @DisplayName("질문을 삭제하면 CascadeType.All로 연관관계 설정된 엔티티도 삭제된다.")
    @Test
    void delete_test() {
        Answer actualAnswer = answerRepository.save(new Answer(u1, q1, "Answers"));
        q1.getAnswers().add(actualAnswer);
        Question question = questionRepository.save(q1);

        Optional<Answer> answer = answerRepository.findById(actualAnswer.getId());
        assertTrue(answer.isPresent());
        questionRepository.delete(question);
        manager.flush();
        Optional<Answer> answer2 = answerRepository.findById(actualAnswer.getId());
        assertFalse(answer2.isPresent());

    }
}