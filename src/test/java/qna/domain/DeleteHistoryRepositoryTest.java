package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Import({UserGenerator.class, QuestionGenerator.class, AnswerGenerator.class})
@DisplayName("Repository:DeleteHistory")
class DeleteHistoryRepositoryTest {

    private final UserGenerator userGenerator;
    private final QuestionGenerator questionGenerator;
    private final AnswerGenerator answerGenerator;
    private final DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryRepositoryTest(
        UserGenerator userGenerator,
        QuestionGenerator questionGenerator,
        AnswerGenerator answerGenerator,
        DeleteHistoryRepository deleteHistoryRepository
    ) {
        this.userGenerator = userGenerator;
        this.questionGenerator = questionGenerator;
        this.answerGenerator = answerGenerator;
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Test
    @DisplayName("질문 삭제 이력 저장")
    public void saveQuestionDeleteHistory() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final DeleteHistory deleteHistory = new DeleteHistory(
            ContentType.QUESTION,
            question.getId(),
            questionWriter.getId(),
            LocalDateTime.now()
        );

        // When
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.isQuestion()).isTrue()
        );
    }

    @Test
    @DisplayName("답변 삭제 이력 저장")
    public void saveAnswerDeleteHistory() {
        // Given
        final User questionWriter = userGenerator.savedUser();
        final Question question = questionGenerator.savedQuestion(questionWriter);
        final User answerWriter = userGenerator.savedUser(UserGenerator.generateAnswerWriter());
        final Answer answer = answerGenerator.savedAnswer(answerWriter, question, "답변 추가");
        final DeleteHistory deleteHistory = new DeleteHistory(
            ContentType.ANSWER,
            answer.getId(),
            answerWriter.getId(),
            LocalDateTime.now()
        );

        // When
        DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

        // Then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.isAnswer()).isTrue()
        );
    }
}
