package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {

    @DisplayName("DeleteHistory 2 개를 가지고 있는 DeleteHistories 의 크기를 확인하면 2 개가 나오는지 확인")
    @Test
    void given_DeleteHistoriesHasTwoElements_when_CheckSizeOfDeleteHistories_then_ReturnTwo() {
        // given
        final DeleteHistories deleteHistories = new DeleteHistories(deleteHistories(writer()));

        // when
        final int actual = deleteHistories.size();

        // then
        assertThat(actual).isEqualTo(2);
    }

    private User writer() {
        return new User("userid", "password", "name", "email");
    }

    private List<DeleteHistory> deleteHistories(final User writer) {
        return Arrays.asList(
            new DeleteHistory(ContentType.QUESTION, 1L, writer, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 1L, writer, LocalDateTime.now()));
    }
}
