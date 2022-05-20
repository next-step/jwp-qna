package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.DeleteHistoryContent.removeAnswer;
import static qna.domain.DeleteHistoryContent.removeQuestion;
import static qna.domain.QuestionTest.Q1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoryContentTest {

    @Test
    @DisplayName("객체 검증 비교")
    void verifyDeleteHistoryContent() {
        assertAll(
                () -> assertThat(removeAnswer(A1)).isEqualTo(removeAnswer(A1)),
                () -> assertThat(removeQuestion(Q1)).isEqualTo(removeQuestion(Q1))
        );
    }
}
