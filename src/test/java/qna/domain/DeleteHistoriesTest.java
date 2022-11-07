package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.fixture.TestFixture.JAVAJIGI;

class DeleteHistoriesTest {

    private DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, JAVAJIGI, LocalDateTime.now());
    }

    @Test
    void add() {
        DeleteHistories deleteHistories = new DeleteHistories();

        deleteHistories.add(deleteHistory);

        assertThat(deleteHistories.getDeleteHistories()).containsExactly(deleteHistory);
    }
}
