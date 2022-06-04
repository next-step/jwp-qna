package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.AnswerGenerator.CONTENTS;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.NotFoundException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

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
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit에 의해 할당되는 수정일시 정보의 할당 여부").isNotNull(),
            () -> assertThat(actual).as("동일 트랜잭션 내 객체 동일성 보장").isSameAs(given)
        );
    }

    @Test
    @DisplayName("답변 저장 시, 작성자 정보가 영속상태가 아닌 경우 예외")
    public void saveAnswer_WhenInvalidWriter() {
        // Given
        final Question question = QuestionGenerator.generateQuestion(UserGenerator.generateQuestionWriter());
        final User answerWriter = UserGenerator.generateAnswerWriter();
        final Answer given = AnswerGenerator.generateAnswer(answerWriter, question, CONTENTS);
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
        final Answer given = AnswerGenerator.generateAnswer(answerWriter, question, CONTENTS);

        // When & Then
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
            .isThrownBy(() -> answerRepository.save(given))
            .as("질문 정보가 영속상태가 아닌 답변");
    }

    @Test
    @DisplayName("삭제되지 않은 특정 질문의 답변 목록 조회")
    public void findByQuestionIdAndDeletedFalseTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());

        answerGenerator.savedAnswer(answerWriter, question, "답변 내용1");
        answerGenerator.savedAnswer(answerWriter, question, "답변 내용2");
        answerGenerator.savedAnswer(answerWriter, question, "답변 내용3");

        // When
        List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(question);

        // Then
        assertThat(actual)
            .hasSize(3)
            .allSatisfy(answer -> assertAll(
                () -> assertThat(answer.getQuestion()).isEqualTo(question),
                () -> assertThat(answer.isDeleted()).as("삭제 상태 False 여부").isFalse()
            ));
    }

    @Test
    @DisplayName("삭제 상태가 아닌 특정 답변 조회")
    public void findByIdAndDeletedFalseTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = answerGenerator.savedAnswer(answerWriter, question, "답변 내용");

        // When
        Answer actual = answerRepository.findByIdAndDeletedFalse(given.getId())
            .orElseThrow(NotFoundException::new);

        // Then
        assertAll(
            () -> assertThat(actual.equals(given)),
            () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("변경 감지에 의한 답변 삭제 상태 변경")
    public void setDeleteTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = answerGenerator.savedAnswer(answerWriter, question, "답변 내용");

        // When
        given.setDeleted(true);
        entityManager.flush();

        // Then
        assertThat(given.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제된 질문에 답변 하는 경우, 고아 객체 발생")
    public void toDeletedQuestionTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        question.setDeleted(true);
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

    @Test
    @DisplayName("답변이 있는 질문 삭제 상태 변경 시, 고아 객체 발생")
    public void changeDeleted_WhenHasAnswer() {
        // Given
        final User questionWriter = userGenerator.savedUser(UserGenerator.generateQuestionWriter());
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer given = answerGenerator.savedAnswer(answerWriter, question, "답변 내용");

        // When
        question.setDeleted(true);
        entityManager.flush();

        // Then
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(given.isDeleted()).isFalse()
        );
    }
}
