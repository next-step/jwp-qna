package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    @Test
    @DisplayName("객체 검증 비교")
    void verifyDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(new DeleteHistory()));

        assertThat(deleteHistories).isEqualTo(new DeleteHistories(Arrays.asList(new DeleteHistory())));
    }
}
