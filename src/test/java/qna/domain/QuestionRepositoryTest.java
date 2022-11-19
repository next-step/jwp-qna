package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private TestEntityManager manager;

    @Autowired
    private EntityManagerFactory factory;

    private User user;

    private Question question;

    @BeforeEach
    void setUp() {
        user = saveUser(new User( "javajigi", "pwd", "name", "test@gmail.com"));
        question = saveQuestion(new Question("JPA", "JPA Content").writeBy(user));
    }

    @AfterEach
    void tearDown() {
        manager.flush();
        manager.clear();
    }

    @DisplayName("삭제되지 않은 질문만을 조회 할 수 있어야 한다")
    @Test
    void find_notDeletedOnly() {
        final Question otherQuestion = new Question("JPA", "JPA Content");
        final Question deletedQuestion = new Question("JPA", "Another JPA Content");
        deletedQuestion.setDeleted(true);

        saveQuestion(otherQuestion);
        saveQuestion(deletedQuestion);

        assertAll(
            () -> assertThat(questionRepository.findByIdAndDeletedFalse(otherQuestion.getId()))
                .isPresent(),
            () -> assertThat(questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId()))
                .isNotPresent()
        );
    }

    @DisplayName("질문 조회시, 해당 질문의 삭제되지 않은 답변만을 조회가능 해야 한다")
    @Test
    void find_containsNotDeletedAnswers() {
        saveAnswer(createAnswer(question, user));
        saveAnswer(createDeletedAnswer(question, user));

        manager.flush();
        manager.clear();

        final Question found = questionRepository.findByIdAndDeletedFalse(question.getId()).get();
        final Answers answers = found.getAnswers();

        assertAll(
            () -> assertThat(answers.getValues()).hasSize(1),
            () -> assertThat(answers.getValues().get(0).isDeleted()).isFalse()
        );
    }

    @DisplayName("질문 조회시, 해당 질문의 삭제되지 않은 답변들은 프록시 객체가 아닌 실제 객체여야 한다")
    @Test
    void find_notProxyAnswers() {
        saveAnswer(createAnswer(question, user));
        saveAnswer(createDeletedAnswer(question, user));

        manager.flush();
        manager.clear();

        PersistenceUnitUtil persistenceUnitUtil = factory.getPersistenceUnitUtil();

        final Question found = questionRepository.findByIdAndDeletedFalse(question.getId()).get();

        assertThat(persistenceUnitUtil.isLoaded(found.getAnswers(), "answers")).isTrue();
    }

    @DisplayName("저장 후 갱신 가능해야 한다")
    @Test
    void update() {
        final Question saved = questionRepository.save(question);
        final User newUser = userRepository.save(new User(2L, "sanjigi", "pwd", "name", "test@gmail.com"));

        Question updated = questionRepository.findById(saved.getId()).get();
        updated.writeBy(newUser);

        final Question actual = questionRepository.findById(saved.getId()).get();
        assertThat(actual.isOwner(newUser)).isTrue();
    }

    @DisplayName("저장 후 삭제 가능 해야 한다")
    @Test
    void delete() {
        final Question saved = questionRepository.save(question);

        questionRepository.deleteById(saved.getId());
        assertThat(questionRepository.findById(saved.getId()).orElse(null)).isNull();
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    private Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    private Answer createAnswer(Question question, User writer) {
        final Answer answer = new Answer(writer, question, String.format("Answer of %s", question.getContents()));
        question.addAnswer(answer);
        return answer;
    }

    private Answer createDeletedAnswer(Question question, User writer) {
        final Answer answer = createAnswer(question, writer);
        answer.setDeleted(true);
        return answer;
    }

}
