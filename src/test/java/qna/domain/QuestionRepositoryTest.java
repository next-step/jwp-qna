package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.QuestionGenerator.CONTENTS;
import static qna.generator.QuestionGenerator.TITLE;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.NotFoundException;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;
import qna.repository.QuestionRepository;

@DataJpaTest
@Import({UserGenerator.class, QuestionGenerator.class, AnswerGenerator.class})
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repository:Question")
class QuestionRepositoryTest {

    private final QuestionRepository questionRepository;
    private final UserGenerator userGenerator;
    private final QuestionGenerator questionGenerator;
    private final AnswerGenerator answerGenerator;
    private final EntityManager entityManager;

    public QuestionRepositoryTest(
        QuestionRepository questionRepository,
        UserGenerator userGenerator,
        QuestionGenerator questionGenerator,
        AnswerGenerator answerGenerator,
        EntityManager entityManager
    ) {
        this.questionRepository = questionRepository;
        this.userGenerator = userGenerator;
        this.questionGenerator = questionGenerator;
        this.answerGenerator = answerGenerator;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("질문 저장")
    public void saveQuestionTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = new Question(TITLE, CONTENTS).writeBy(questionWriter);

        // When
        Question actual = questionRepository.save(given);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).as("IDENTITY 전략에 따라 DB에서 부여된 PK값 생성 여부").isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.isOwner(questionWriter)).isTrue(),
            () -> assertThat(given.getCreatedAt()).as("JPA Audit에 의해 할당되는 생성일시 정보의 할당 여부").isNotNull(),
            () -> assertThat(given.getUpdatedAt()).as("JPA Audit의 modifyOnCreate 설정에 의한 수정일시 정보 Null 여부").isNull()
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("유효하지 못한 질문자 정보를 가지는 질문 저장 시 예외")
    public void saveQuestion_WhenInvalidWriter(
        final Question given,
        final String throwDescription
    ) {
        // When & Then
        assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
            .isThrownBy(() -> questionRepository.save(given))
            .as(throwDescription)
            .isInstanceOf(RuntimeException.class);
    }

    private static Stream saveQuestion_WhenInvalidWriter() {
        final Question given = new Question(TITLE, CONTENTS);
        return Stream.of(
            Arguments.of(given, "질문 작성자 정보가 없는 질문"),
            Arguments.of(given.writeBy(UserGenerator.generateQuestionWriter()), "영속 상태가 아닌 질문 작성자 정보를 가진 질문")
        );
    }

    @Test
    @DisplayName("질문 번호를 이용한 특정 질문 조회")
    public void findByIdTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);
        entityManager.clear();

        // When
        Question actual = questionRepository.findById(given.getId()).orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual.getWriter())
            .as("LazyLoading 옵션 적용으로 인한 *ToOne 연관관계인 질문 작성자 엔티티의 프록시 객체 여부")
            .isInstanceOf(HibernateProxy.class);
    }

    @Test
    @DisplayName("삭제 상태가 아닌 특정 질문 조회")
    public void findByIdAndDeletedFalseTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);
        entityManager.clear();

        // When
        Question actual = questionRepository.findByIdAndDeletedFalse(given.getId()).orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual.isDeleted()).as("삭제 상태 False 여부").isFalse();
    }

    @Test
    @DisplayName("삭제 상태가 아닌 질문 목록 조회")
    public void findByDeletedFalseTest() {
        final User questionWriter = userGenerator.savedUser();
        questionGenerator.savedQuestion(questionWriter);
        questionGenerator.savedQuestion(questionWriter);
        entityManager.clear();

        // When
        List<Question> actual = questionRepository.findByDeletedFalse();

        // Then
        assertThat(actual)
            .hasSize(2)
            .allSatisfy(question ->
                assertAll(
                    () -> assertThat(question.isDeleted())
                        .as("조회된 질문 목록의 상제 상태 False 여부")
                        .isFalse(),
                    () -> assertThat(question.getWriter())
                        .as("LazyLoading 옵션 적용으로 인한 *ToOne 연관관계인 질문 작성자 엔티티의 프록시 객체 여부")
                        .isInstanceOf(HibernateProxy.class)
                )
            );
    }

    @Test
    @DisplayName("변경 감지에 의한 삭제 여부 수정")
    public void setDeleteTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);

        // When
        given.setDeleted(true);
        entityManager.flush();

        // Then
        assertAll(
            () -> assertThat(given.isDeleted()).isTrue(),
            () -> assertThat(given.getUpdatedAt()).as("flush 시점에 @PreUpdate 콜백 메서드에 의한 생성일시 값 할당 여부")
                .isNotNull()
        );
    }
    
    @Test
    @DisplayName("질문의 답변 목록 조회 시, 미 삭제 상태 여부 검증")
    public void deletedFalseAnswersToQuestion(){
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);
        answerGenerator.savedAnswer(questionWriter, given);
        answerGenerator.savedAnswer(questionWriter, given);
        answerGenerator.savedDeleteAnswer(questionWriter, given);
        entityManager.clear();

        // When
        Question question = questionRepository.findById(given.getId())
            .orElseThrow(NotFoundException::new);
        List<Answer> actual = question.getAnswers();

        // Then
        assertThat(actual)
            .hasSize(2)
            .allSatisfy(answer -> assertThat(answer.isDeleted()).isFalse());
    }
}
