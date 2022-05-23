package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {
    private DeleteHistories deleteHistories;

    @BeforeEach
    void setUp() {
        List<DeleteHistory> data = Arrays.asList(
                new DeleteHistory(),
                new DeleteHistory(),
                new DeleteHistory()
        );

        deleteHistories = DeleteHistories.from(data);
    }

    @Test
    @DisplayName("DeleteHistory의 개수를 확인한다.")
    void 개수_확인() {
        assertThat(deleteHistories.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("DeleteHistory를 추가한다.")
    void 추가() {
        deleteHistories.add(new DeleteHistory());
        assertThat(deleteHistories.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("DeleteHistories의 모든 DeleteHistory를 추가한다.")
    void 모두_추가() {
        List<DeleteHistory> data = Arrays.asList(
                new DeleteHistory(),
                new DeleteHistory()
        );
        DeleteHistories newDeleteHistories = DeleteHistories.from(data);

        deleteHistories.addAll(newDeleteHistories);
        assertThat(deleteHistories.get()).hasSize(5);
    }
}
