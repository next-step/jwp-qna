package qna.domain;

import java.util.Collections;
import java.util.RandomAccess;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import antlr.collections.List;

public class DeleteHistoriesTest {
    DeleteHistories deleteHistories;

    @BeforeEach
    public void beforeEach() {
        deleteHistories = DeleteHistories.valueOf();
    }

    @DisplayName("삭제 이력이 추가된다.")
    @Test
    public void add_DeleteHistory() {
        // given
        DeleteHistory deleteHistory = DeleteHistoryTest.deleteHistory1;

        // when
        deleteHistories.add(deleteHistory);

        // then
        Assertions.assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @DisplayName("삭제 이력들의 인스턴스를 List 객체로 변경한다.")
    @Test
    public void changeType_toList() {
        // given
        DeleteHistory deleteHistory = DeleteHistoryTest.deleteHistory1;

        // when
        deleteHistories.add(deleteHistory);

        // then
        Assertions.assertThat(deleteHistories.toList()).isInstanceOf(RandomAccess.class);
        Assertions.assertThat(deleteHistories.toList().size()).isEqualTo(1);
    }
}
