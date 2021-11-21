package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    @DisplayName("삭제 이력 추가")
    @Test
    void addAll() {
        DeleteHistories deleteHistories = new DeleteHistories(new DeleteHistory(), new DeleteHistory());
        deleteHistories.addAll(new DeleteHistories(Arrays.asList(new DeleteHistory(), new DeleteHistory())));

        assertThat(deleteHistories).isEqualTo(new DeleteHistories(
            Arrays.asList(new DeleteHistory(), new DeleteHistory(), new DeleteHistory(), new DeleteHistory())));
    }
}