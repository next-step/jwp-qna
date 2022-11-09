package qna.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    @DisplayName("DeleteHistory를 추가할 수 있다.")
    @Test
    void add() {
        DeleteHistories deleteHistories = new DeleteHistories();
        DeleteHistory deleteHistory = DeleteHistory.of(ContentType.ANSWER, 1L, UserTest.SANJIGI);

        deleteHistories.add(deleteHistory);

        Assertions.assertThat(deleteHistories.getUnmodifiableDeleteHistories()).containsExactly(deleteHistory);
    }

    @DisplayName("getUnmodifiableDeleteHistories()한 List를 수정하면 UnsupportedOperationException이 발생한다.")
    @Test
    void unmodifiable_list_delete() {
        DeleteHistories deleteHistories = new DeleteHistories();
        DeleteHistory deleteHistory = DeleteHistory.of(ContentType.ANSWER, 1L, UserTest.SANJIGI);

        List<DeleteHistory> unmodifiableDeleteHistories = deleteHistories.getUnmodifiableDeleteHistories();

        Assertions.assertThatThrownBy(() -> unmodifiableDeleteHistories.add(deleteHistory))
                .isInstanceOf(UnsupportedOperationException.class);

    }
}
