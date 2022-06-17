package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.CannotDeleteException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;
import qna.repository.AnswerRepository;

@DataJpaTest
@Import({UserGenerator.class, QuestionGenerator.class, AnswerGenerator.class})
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repository:Answer")
class AnswerRepositoryTest {

    private final AnswerRepository answerRepository;
    private final UserGenerator userGenerator;
    private final QuestionGenerator questionGenerator;
    private final AnswerGenerator answerGenerator;
    private final EntityManager entityManager;

    public AnswerRepositoryTest(
        AnswerRepository answerRepository,
        UserGenerator userGenerator,
        QuestionGenerator questionGenerator,
        AnswerGenerator answerGenerator,
        EntityManager entityManager
    ) {
        this.answerRepository = answerRepository;
        this.userGenerator = userGenerator;
        this.questionGenerator = questionGenerator;
        this.answerGenerator = answerGenerator;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("답변 저장")
    public void saveAnswerTest() {
        // Given
        final User questionWriter = userGenerator.savedUser(UserGenerator.generateQuestionWriter());
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = new Answer(answerWriter, question, "답변 내용");
        given.toQuestion(question);

        // When
        Answer actual = answerRepository.save(given);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).as("IDENTITY 전략에 따라 DB에서 부여된 PK값 생성 여부").isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.isOwner(answerWriter)).isTrue(),
            () -> assertThat(actual.getQuestion()).isEqualTo(question),
            () -> assertThat(given.getCreatedAt()).as("JPA Audit에 의해 할당되는 생성일시 정보의 할당 여부").isNotNull(),
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit의 modifyOnCreate 설정에 의한 수정일시 정보 Null 여부").isNull(),
            () -> assertThat(actual).as("동일 트랜잭션 내 객체 동일성 보장").isSameAs(given)
        );
    }

    @Test
    @DisplayName("답변 저장 시, 작성자 정보가 영속상태가 아닌 경우 예외")
    public void saveAnswer_WhenInvalidWriter() {
        // Given
        final Question question = QuestionGenerator.generateQuestion(UserGenerator.generateQuestionWriter());
        final User answerWriter = UserGenerator.generateAnswerWriter();
        final Answer given = AnswerGenerator.generateAnswer(answerWriter, question);
        given.toQuestion(question);

        // When
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
            .isThrownBy(() -> answerRepository.save(given))
            .as("작성자 정보가 영속상태가 아닌 답변");
    }

    @Test
    @DisplayName("답변 저장 시, 질문 정보가 영속상태가 아닌 경우 예외")
    public void saveAnswer_WhenInvalidQuestion() {
        // Given
        final Question question = QuestionGenerator.generateQuestion(UserGenerator.generateQuestionWriter());
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = AnswerGenerator.generateAnswer(answerWriter, question);

        // When & Then
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
            .isThrownBy(() -> answerRepository.save(given))
            .as("질문 정보가 영속상태가 아닌 답변");
    }

    @Test
    @DisplayName("변경 감지에 의한 답변 삭제 상태 변경")
    public void setDeleteTest() throws CannotDeleteException {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final Answer given = answerGenerator.savedAnswer(questionWriter, question);

        // When
        given.delete(questionWriter);
        entityManager.flush();

        // Then
        assertAll(
            () -> assertThat(given.isDeleted()).isTrue(),
            () -> assertThat(given.getUpdatedAt()).as("flush 시점에 @PreUpdate 콜백 메서드에 의한 생성일시 값 할당 여부")
                .isNotNull()
        );
    }

    @Test
    @DisplayName("본인이 작성하지 않은 답변 삭제 시 예외 발생 검증")
    public void setDeleteTestException() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = answerGenerator.savedAnswer(answerWriter, question);

        // When & Then
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> given.delete(questionWriter));
    }

    @Test
    @DisplayName("삭제된 질문에 답변 하는 경우, 고아 객체 발생")
    public void toDeletedQuestionTest() throws CannotDeleteException {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        question.delete(questionWriter);
        entityManager.flush();

        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = new Answer(answerWriter, question, "답변 내용");

        // When
        given.toQuestion(question);
        Answer actual = answerRepository.save(given);

        // Then
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }
}
