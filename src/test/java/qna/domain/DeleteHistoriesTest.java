package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    @Test
    @DisplayName("객체 검증 비교")
    void verifyDeleteHistories() {
        DeleteHistoryContent deleteHistoryContent = DeleteHistoryContent.removeQuestion(Q1);
        List<DeleteHistory> deleteHistoryList = Collections.singletonList(
                new DeleteHistory(deleteHistoryContent, JAVAJIGI));
        DeleteHistories deleteHistories = new DeleteHistories(deleteHistoryList);

        assertThat(deleteHistories).isEqualTo(new DeleteHistories(deleteHistoryList));
    }
}
