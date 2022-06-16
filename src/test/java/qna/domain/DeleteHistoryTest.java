package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
