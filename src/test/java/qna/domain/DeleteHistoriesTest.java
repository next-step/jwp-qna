package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {

    @DisplayName("답변이 없을 경우 질문 히스토리만 생성")
    @Test
    void createQuestionHistory() {
        DeleteHistories deleteHistories = DeleteHistories.of(QuestionTest.Q1, null);
        assertThat(deleteHistories.getDeleteHistories()).isNotOfAnyClassIn(Answer.class);
    }

}