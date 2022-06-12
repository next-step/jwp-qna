package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoriesTest {

    @DisplayName("add DeleteHistory 함수 테스트")
    @Test
    void addTest01() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, 0L, UserTest.JAVAJIGI, LocalDateTime.now()));
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now()));

        assertThat(deleteHistories.getList())
                .isNotNull()
                .hasSize(2);
    }

    @DisplayName("add DeleteHistories 함수 테스트")
    @Test
    void addTest02() {
        DeleteHistories target = new DeleteHistories();
        target.add(new DeleteHistory(ContentType.QUESTION, 0L, UserTest.JAVAJIGI, LocalDateTime.now()));
        target.add(new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now()));
        target.add(new DeleteHistory(ContentType.QUESTION, 2L, UserTest.JAVAJIGI, LocalDateTime.now()));

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(target);

        assertThat(deleteHistories.getList())
                .isNotNull()
                .hasSize(3);
    }
}