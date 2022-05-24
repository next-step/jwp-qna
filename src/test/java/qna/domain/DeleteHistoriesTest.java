package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DeleteHistoriesTest {
    @Test
    @DisplayName("DeleteHistories 생성")
    void DeleteHistories_생성(){
        DeleteHistories deleteHistories = DeleteHistories.of(QuestionTest.Q1, QuestionTest.Q1.getWriter());
        assertThat(deleteHistories.getUnmodifiableDeleteHistories().size()).isEqualTo(1);
    }
}
