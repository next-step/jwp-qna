package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.fixture.TestAnswerFactory;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManagerFactory factory;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("답변을 저장할 수 있다")
    @Test
    void save() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = TestAnswerFactory.create(writer, question);

        Answer result = answerRepository.save(expect);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.isDeleted()).isEqualTo(expect.isDeleted()),
                () -> assertThat(result.getContents()).isEqualTo(expect.getContents()),
                () -> assertThat(result.getQuestion()).isEqualTo(expect.getQuestion()),
                () -> assertThat(result.getWriter()).isEqualTo(expect.getWriter())
        );
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findById() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = answerRepository.save(TestAnswerFactory.create(writer, question));

        Answer result = answerRepository.findByIdAndDeletedFalse(expect.getId()).get();

        assertEquals(expect, result);
    }

    @DisplayName("답변이 삭제되었을 경우, findByIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedById() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));
        answer.softDelete();

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertThat(result).isNotPresent();
    }

    @DisplayName("질문의 id로 조회할 수 있다")
    @Test
    void findByQuestionId() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = answerRepository.save(TestAnswerFactory.create(writer, question));

        Answer result = answerRepository.findByQuestionIdAndDeletedFalse(expect.getQuestion().getId()).get(0);

        assertEquals(expect, result);
    }

    @DisplayName("답변이 삭제되었을 경우, findByQuestionIdAndDeletedFalse 함수로 조회할 수 없다")
    @Test
    void findDeletedByQuestionId() {
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));
        answer.softDelete();

        List<Answer> result = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        assertThat(result).isEmpty();
    }

    @DisplayName("답변 조회시 writer, question이 지연로딩 되는지 확인한다")
    @Test
    void lazyLoadingTest() {
        PersistenceUnitUtil persistenceUnitUtil = factory.getPersistenceUnitUtil();
        User writer = userRepository.save(TestUserFactory.create("writer"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        answerRepository.save(TestAnswerFactory.create(writer, question));
        testEntityManager.flush();
        testEntityManager.clear();

        Answer result = answerRepository.findAll().get(0);

        assertThat(persistenceUnitUtil.isLoaded(result, "writer")).isFalse();
        assertThat(persistenceUnitUtil.isLoaded(result, "question")).isFalse();
    }

    @DisplayName("다른 사람이 답변을 삭제를 시도하면 예외가 발생한다")
    @Test
    void noOwnerDeleteException() {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        User hacker = userRepository.save(TestUserFactory.create("나쁜놈"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));

        assertThatThrownBy(() -> answer.delete(hacker))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("답변을 삭제할 수 있다")
    @Test
    void delete() throws CannotDeleteException {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer answer = answerRepository.save(TestAnswerFactory.create(writer, question));

        answer.delete(writer);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    @DisplayName("답변이 삭제되면, 삭제 이력이 반환된다")
    @Test
    void deleteHistory() throws CannotDeleteException {
        User writer = userRepository.save(TestUserFactory.create("서정국"));
        Question question = questionRepository.save(TestQuestionFactory.create(writer));
        Answer expect = answerRepository.save(TestAnswerFactory.create(writer, question));

        DeleteHistory result = expect.delete(writer);

        assertThat(result.getContentId()).isEqualTo(expect.getId());
    }
}
