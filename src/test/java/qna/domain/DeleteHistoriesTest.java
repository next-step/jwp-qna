package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Distory_List_일급객체_테스트")
public class DeleteHistoriesTest {
    private DeleteHistory deleteHistory;

    @BeforeEach
    void before() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    }

    @DisplayName("deleteHistories_add_테스트")
    @Test
    void add() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(deleteHistory);
        assertThat(deleteHistories.getDeleteHistories()).containsExactly(deleteHistory);
    }
}
