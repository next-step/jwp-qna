package qna.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoriesTest {

    private DeleteHistories deleteHistories;
    private DeleteHistory deleteHistory;

    @BeforeEach
    void init() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        deleteHistories = new DeleteHistories();
    }

    @Test
    @DisplayName("DeleteHistory 추가")
    void DeleteHistories_add() {
        deleteHistories.add(deleteHistory);

        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }



}
