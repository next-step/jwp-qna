package qna.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
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
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestEntityManager manager;

    @Autowired
    private EntityManagerFactory factory;


    private User u1;
    private User u2;
    private Question q1;
    private Question q2;
    private Answer a1;
    private Answer a2;

    @BeforeEach
    void setup() {
        u1 = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        u2 = userRepository.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));
        q1 = questionRepository.save(new Question("title1", "contents1").writeBy(u1));
        q2 = questionRepository.save(new Question("title2", "contents2").writeBy(u2));
        a1 = new Answer(u1, q1, "contents");
        a2 = new Answer(u2, q2, "contents");
    }

    @DisplayName("답변을 생성할 수 있다.")
    @Test
    void save_test() {
        Answer actual = answerRepository.save(a1);
        assertAll(
                () -> assertNotNull(actual.getId()),
                () -> assertNotNull(actual.getWriter().getId()),
                () -> assertNotNull(actual.getQuestion().getId()),
                () -> assertNotNull(userRepository.findByUserId(u1.getUserId()).get())
        );
    }

    @DisplayName("답변의 작성자를 변경할 수 있다.")
    @Test
    void update_cascade_test() {
        Answer actual = answerRepository.save(a1);

        actual.setWriter(u2);

        assertAll(
                () -> assertNotNull(actual.getId()),
                () -> assertEquals(actual.getWriter().getEmail(), u2.getEmail()),
                () -> assertEquals(actual.getQuestion().getWriter(), q1.getWriter()),
                () -> assertTrue(userRepository.findByUserId(u2.getUserId()).isPresent())
        );
    }

    @DisplayName("전체 답변 및 답변의 작성자와 질문을 조회할 수 있다.")
    @Test
    void findAll_test() {
        answerRepository.save(a1);
        answerRepository.save(a2);

        List<Answer> answers = answerRepository.findAll();

        assertAll(
                () -> assertEquals(answers.size(), 2),
                () -> assertNotNull(answers.get(0).getWriter().getId()),
                () -> assertNotNull(answers.get(1).getWriter().getId()),
                () -> assertNotNull(answers.get(0).getQuestion().getId()),
                () -> assertNotNull(answers.get(1).getQuestion().getId())
        );
    }

    @DisplayName("특정 답변/작성자/질문을 동시에 조회할 수 있다.")
    @Test
    void find_test() {

        answerRepository.save(a1);

        Optional<Answer> answer = answerRepository.findById(a1.getId());

        assertAll(
                () -> assertTrue(answer.isPresent()),
                () -> assertNotNull(answer.get().getWriter().getId()),
                () -> assertNotNull(answer.get().getQuestion().getId())
        );
    }


    @DisplayName("lazy로드는 지연로딩된 원소에 접근할때 실제 DB에서 가져온다.")
    @Test
    void lazy_loading_test() {
        PersistenceUnitUtil persistenceUnitUtil = factory.getPersistenceUnitUtil();
        answerRepository.save(a1);
        manager.clear();
        Optional<Answer> answer = answerRepository.findById(a1.getId());
        assertThat(persistenceUnitUtil.isLoaded(answer.get(), "question")).isFalse();
        Question question = answer.get().getQuestion();
        assertThat(persistenceUnitUtil.isLoaded(answer.get(), "question")).isFalse();
        User writer = answer.get().getQuestion().getWriter();
        assertThat(persistenceUnitUtil.isLoaded(answer.get(), "question")).isTrue();
    }

    @DisplayName("답변을 삭제할 수 있다")
    @Test
    void delete_test() {
        Answer actual = answerRepository.save(a1);
        Long id = actual.getId();
        answerRepository.delete(actual);

        Optional<Answer> answer = answerRepository.findById(id);

        assertFalse(answer.isPresent());
    }

    @DisplayName("CascadeType.REMOVE를 설정하지 않으면 답변을 삭제해도 부모 엔티티가 삭제 되지 않는다.")
    @Test
    void delete_cascade_test() {
        Answer actual = answerRepository.save(a1);

        Long id = actual.getId();
        answerRepository.delete(actual);
        Optional<Answer> answer = answerRepository.findById(id);
        Optional<User> user = userRepository.findByUserId(u1.getUserId());

        assertFalse(answer.isPresent());
        assertTrue(user.isPresent());
    }

    @DisplayName("삭제되지 않은 답변을 질문아이디로 조회할 수 있다")
    @Test
    void findByQuestionIdAndDeletedFalse_test() {
        Answer answer = answerRepository.save(a1);
        Long questionId = answer.getQuestion().getId();

        List<Answer> beforeDeleted = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        answer.setDeleted(true);

        List<Answer> afterDeleted = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        assertAll(
                () -> assertThat(beforeDeleted.get(0)).isEqualTo(answer),
                () -> assertEquals(afterDeleted.size(), 0)
        );
    }

    @DisplayName("삭제되지 않은 특정 답변을 조회할 수 있다")
    @Test
    void findByIdAndDeletedFalse_test() {
        Answer answer1 = answerRepository.save(a1);
        Answer answer2 = answerRepository.save(a2);

        answer2.setDeleted(true);

        Optional<Answer> expect1 = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        Optional<Answer> expect2 = answerRepository.findByIdAndDeletedFalse(answer2.getId());

        assertAll(
                () -> assertTrue(expect1.isPresent()),
                () -> assertFalse(expect2.isPresent())
        );
    }
}