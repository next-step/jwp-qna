package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixture.UserTestFixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {
    @Test
    @DisplayName("삭제 이력 생성")
    void 생성() {
        List<DeleteHistory> deleteHistoryList = Arrays.asList(
                new DeleteHistory(ContentType.QUESTION, 1L, UserTestFixture.JAVAJIGI, LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, 2L, UserTestFixture.JAVAJIGI, LocalDateTime.now())
        );
        DeleteHistories deleteHistories = new DeleteHistories(deleteHistoryList);

        assertThat(deleteHistories).isEqualTo(new DeleteHistories(deleteHistoryList));
        assertThat(deleteHistories.unmodifiedDeleteHistories()).containsAll(deleteHistoryList);
    }
}
