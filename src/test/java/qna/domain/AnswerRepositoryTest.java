package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager entityManager;

    private User savedUser;
    private Question savedQuestion;
    private Answer givenAnswer;
    private Answer savedAnswer;

    @BeforeEach
    void setUp() {
        User user = new User("bjr", "password", "name", "email");
        savedUser = userRepository.save(user);

        Question question = new Question("question", "질문내용").writeBy(savedUser);
        savedQuestion = questionRepository.save(question);

        givenAnswer = new Answer(savedUser, savedQuestion, AnswerTest.A1.getContents());
        savedAnswer = answerRepository.save(givenAnswer);
    }

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(givenAnswer.getWriter()),
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(givenAnswer.getQuestion()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(givenAnswer.getContents())
        );
    }

    @Test
    @DisplayName("User 매핑된 참조 부분 지연로딩인지 테스트(즉시로딩 안되는지 테스트)")
    void testForUserLazy() {
        entityManager.clear();
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Optional<Answer> maybeAnswer = answerRepository.findById(savedAnswer.getId());

        boolean isLoaded = persistenceUnitUtil.isLoaded(maybeAnswer.get(), "writer");
        assertThat(isLoaded).isFalse(); //지연로딩이라서 여기선 false

        String writerName = maybeAnswer.get().getWriter().getName();
        assertAll(
                () -> assertThat(writerName).isEqualTo(savedUser.getName()),
                () -> assertThat(persistenceUnitUtil.isLoaded(maybeAnswer.get(), "writer")).isTrue()
        );
    }

    @Test
    @DisplayName("Question 매핑된 참조 부분 지연로딩인지 테스트(즉시로딩 안되는지 테스트)")
    void testForQuestionLazy() {
        entityManager.clear();
        PersistenceUnitUtil persistenceUnitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Optional<Answer> maybeAnswer = answerRepository.findById(savedAnswer.getId());

        boolean isLoaded = persistenceUnitUtil.isLoaded(maybeAnswer.get(), "question");
        assertThat(isLoaded).isFalse(); //지연로딩이라서 여기선 false

        String questionTitle = maybeAnswer.get().getQuestion().getTitle();
        assertAll(
                () -> assertThat(questionTitle).isEqualTo(savedQuestion.getTitle()),
                () -> assertThat(persistenceUnitUtil.isLoaded(maybeAnswer.get(), "question")).isTrue()
        );
    }

    @Test
    @DisplayName("answer 수정 테스트")
    void update() {
        String changedContents = "내용 바꿔보기";
        savedAnswer.setContents(changedContents);

        Optional<Answer> updatedAnswer = answerRepository.findById(savedAnswer.getId());

        assertThat(updatedAnswer.get().getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("answer 제거 테스트")
    void delete() {
        answerRepository.delete(savedAnswer);

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers.contains(savedAnswer)).isFalse();
    }

    @Test
    @DisplayName("questionId로 매칭되는 질문에 대한 답변들 정상 조회하는지 테스트")
    void findByQuestionIdAndDeletedFalse() {
        Answer givenAnswer_2 = new Answer(savedUser, savedQuestion, AnswerTest.A2.getContents());
        answerRepository.save(givenAnswer_2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(answers).contains(givenAnswer, givenAnswer_2)
        );
    }

    //
    @Test
    @DisplayName("questionId로 매칭되는 answer값 삭제처리 되었을 시 조회 불가 테스트")
    void findByQuestionIdAndDeletedFalse_failCase() {
        Answer givenAnswer_2 = new Answer(savedUser, savedQuestion, AnswerTest.A2.getContents());
        Answer savedAnswer_2 = answerRepository.save(givenAnswer_2);
        savedAnswer_2.setDeleted(true);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(answers).contains(givenAnswer),
                () -> assertThat(answers).doesNotContain(givenAnswer_2)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 정상 조회하는지 테스트")
    public void findByIdAndDeletedFalse() {
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertAll(
                () -> assertThat(findedAnswer.isPresent()).isTrue(),
                () -> assertThat(findedAnswer.get()).isEqualTo(givenAnswer)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 삭제시 조회 불가 테스트")
    public void findByIdAndDeletedFalse_failCase() {
        savedAnswer.setDeleted(true);
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());

        assertThat(findedAnswer.isPresent()).isFalse();
    }
}
