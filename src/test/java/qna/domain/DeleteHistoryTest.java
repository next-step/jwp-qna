package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.generator.AnswerGenerator;
import qna.generator.QuestionGenerator;
import qna.generator.UserGenerator;

@DisplayName("Domain:DeleteHistory")
class DeleteHistoryTest {

    @Test
    @DisplayName("삭제 이력 생성")
    public void createDeleteHistoryTest() {
        // Given
        final ContentType contentType = ContentType.QUESTION;
        final User questionWriter = UserGenerator.generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        final LocalDateTime createDate = LocalDateTime.now();

        // When
        DeleteHistory actual = new DeleteHistory(contentType, question.getId(), questionWriter, createDate);

        // Then
        assertAll(
            () -> assertThat(actual.isQuestion()).isTrue(),
            () -> assertThat(actual).isEqualTo(new DeleteHistory(contentType, question.getId(), questionWriter, createDate))
        );
    }

    @Test
    @DisplayName("답변이 없는 질문 삭제 시, 삭제 이력 생성")
    public void createDeleteHistory() {
        // Given
        final User questionWriter = UserGenerator.generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);

        // When
        DeleteHistories actual = DeleteHistories.of(question);

        // Then
        assertThat(actual.getSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("답변이 포함된 질문 삭제 시, 삭제 이력 생성")
    public void createDeleteHistories() {
        // Given
        final User questionWriter = UserGenerator.generateQuestionWriter();
        final Question question = QuestionGenerator.generateQuestion(questionWriter);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);
        AnswerGenerator.generateAnswer(questionWriter, question);

        // When
        DeleteHistories actual = DeleteHistories.of(question);

        // Then
        assertThat(actual.getSize()).isEqualTo(4);
    }
}
