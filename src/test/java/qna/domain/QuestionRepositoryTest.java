package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Question question;
    private Question newQuestion;
    private Question saved;

    @BeforeEach
    void setUp() {
        user = new User("userId", "password", "name", "email");
        User savedUser = userRepository.save(user);

        question = new Question("질문1", "질문이 있습니다.").writeBy(savedUser);
        newQuestion = new Question("새로운 질문", "질문이 또 있습니다.").writeBy(savedUser);
        saved = questionRepository.save(question);
    }

    @Test
    @DisplayName("User 매핑된 참조 부분 지연로딩인지 테스트(즉시로딩 안되는지 테스트)")
    void testForUserLazy() {
        entityManager.clear();
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Optional<Question> maybeQuestion = questionRepository.findById(saved.getId());

        boolean isLoaded = persistenceUnitUtil.isLoaded(maybeQuestion.get(), "writer");
        assertThat(isLoaded).isFalse(); //지연로딩이라서 여기선 false

        String writerName = maybeQuestion.get().getWriter().getName();
        assertAll(
                () -> assertThat(writerName).isEqualTo(user.getName()),
                () -> assertThat(persistenceUnitUtil.isLoaded(maybeQuestion.get(), "writer")).isTrue()
        );
    }

    @Test
    @DisplayName("Question 저장 테스트")
    void save() {
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved).isSameAs(question),
                () -> assertThat(saved.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(saved.getTitle()).isEqualTo(question.getTitle())
        );
    }

    @Test
    @DisplayName("Question 수정 테스트")
    void update() {
        String changedContents = "질문 내용 바꾸기";
        saved.updateContents(changedContents);

        Optional<Question> updated = questionRepository.findById(saved.getId());

        assertThat(updated.get().getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("Question 제거 테스트")
    void delete() {
        questionRepository.delete(saved);

        List<Question> questions = questionRepository.findAll();

        assertThat(questions.contains(saved)).isFalse();
    }

    @Test
    @DisplayName("전체 question 조회 테스트")
    void findByDeletedFalse() {
        questionRepository.save(newQuestion);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
        assertThat(questions).contains(saved, newQuestion);
    }

    @Test
    @DisplayName("전체 questions에서 question이 삭제된 경우 조회 실패 테스트")
    void findByDeletedFalse_failCase() {
        Question newSaved = questionRepository.save(newQuestion);

        newSaved.setDeleted(true);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(1);
        assertThat(questions).doesNotContain(newSaved);
    }

    @Test
    @DisplayName("question 아이디 값으로 조회 테스트")
    void findByIdAndDeletedFalse() {
        Optional<Question> finded = questionRepository.findByIdAndDeletedFalse(saved.getId());

        assertAll(
                () -> assertThat(finded.isPresent()).isTrue(),
                () -> assertThat(finded.get()).isEqualTo(saved)
        );
    }

    @Test
    @DisplayName("question 아이디 값으로 조회시 삭제되었으면 조회 실패 테스트")
    void findByIdAndDeletedFalse_failCase() {
        saved.setDeleted(true);

        Optional<Question> finded = questionRepository.findByIdAndDeletedFalse(saved.getId());

        assertThat(finded.isPresent()).isFalse();
    }
}
