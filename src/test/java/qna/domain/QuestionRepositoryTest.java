package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.NotFoundException;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

/**
 * @author : choi-ys
 * @date : 2022/05/26 3:22 오후
 */
@DataJpaTest
@Import({UserGenerator.class, QuestionGenerator.class})
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repository:Question")
class QuestionRepositoryTest {

    private final QuestionRepository questionRepository;
    private final UserGenerator userGenerator;
    private final QuestionGenerator questionGenerator;
    private final EntityManager entityManager;

    public QuestionRepositoryTest(
        QuestionRepository questionRepository,
        UserGenerator userGenerator,
        QuestionGenerator questionGenerator,
        EntityManager entityManager
    ) {
        this.questionRepository = questionRepository;
        this.userGenerator = userGenerator;
        this.questionGenerator = questionGenerator;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("질문 저장")
    public void saveQuestionTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = new Question("title", "contents").writeBy(questionWriter);

        // When
        Question actual = questionRepository.save(given);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).as("IDENTITY 전략에 따라 DB에서 부여된 PK값 생성 여부").isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.isOwner(questionWriter)).isTrue(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getUpdatedAt()).isNull()
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("유효하지 못한 질문자 정보를 가지는 질문 저장 시 예외")
    public void saveQuestion_WhenInvalidWriter(final Question given, final User questionWriter, final String throwDescription) {
        // When
        Question actual = questionRepository.save(given);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.isDeleted()).isFalse(),
            () -> assertThat(actual.getUpdatedAt()).isNull()
        );
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> assertThat(actual.isOwner(questionWriter)).isNull())
            .as(throwDescription);
    }

    private static Stream saveQuestion_WhenInvalidWriter() {
        return Stream.of(
            Arguments.of(new Question("TITLE", "CONTENTS"), null, "질문 작성자 정보가 없는 질문"),
            Arguments.of(new Question("TITLE", "CONTENTS"), UserGenerator.generateQuestionWriter(), "영속 상태가 아닌 질문 작성자 정보를 가진 질문")
        );
    }

    @Test
    @DisplayName("질문 번호를 이용한 특정 질문 조회")
    public void findByIdTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);

        // When
        Question actual = questionRepository.findById(given.getId()).orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual).as("동일 트랜잭션 내 객체 동일성 보장 여부").isSameAs(given);
    }

    @Test
    @DisplayName("삭제 상태가 아닌 특정 질문 조회")
    public void findByIdAndDeletedFalseTest() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question given = questionGenerator.savedQuestion(questionWriter);

        // When
        Question actual = questionRepository.findByIdAndDeletedFalse(given.getId()).orElseThrow(NotFoundException::new);

        // Then
        assertThat(actual).isSameAs(given);
    }

    @Test
    @DisplayName("삭제 상태가 아닌 질문 목록 조회")
    public void findByDeletedFalseTest() {
        final User questionWriter = userGenerator.savedUser();
        questionGenerator.savedQuestion(questionWriter);
        questionGenerator.savedQuestion(questionWriter);

        // When
        List<Question> actual = questionRepository.findByDeletedFalse();

        // Then
        assertThat(actual)
            .hasSize(2)
            .allSatisfy(question -> assertThat(question.isDeleted()).isFalse());
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
        assertThat(given.isDeleted()).isTrue();
    }
}
