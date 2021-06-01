package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {
    private User user1;
    private User user2;
    private DeleteHistories deleteHistories;
    private DeleteHistories formerHistories;
    private DeleteHistory deleteHistory1;
    private DeleteHistory deleteHistory2;
    private DeleteHistory deleteHistory3;

    @BeforeEach
    void setUp() {
        user1 = new User("user1", "password", "user", "user1@test.com");
        user2 = new User("user2", "password", "user2", "user2@test.com");
        deleteHistory1 = DeleteHistory.ofQuestion(1L, user1);
        deleteHistory2 = DeleteHistory.ofAnswer(1L, user2);
        deleteHistory3 = DeleteHistory.ofQuestion(2L, user1);

        List<DeleteHistory> histories = Arrays.asList(deleteHistory2, deleteHistory3);
        deleteHistories = new DeleteHistories();
        formerHistories = new DeleteHistories(histories);
    }

    @Test
    void addHistory() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(1L, user1);

        deleteHistories.addHistory(deleteHistory);

        assertThat(deleteHistories.getHistories()).containsExactly(deleteHistory);
    }

    @Test
    void addAllHistories() {
        deleteHistories.addHistory(deleteHistory1);

        DeleteHistories actual = deleteHistories.addAll(formerHistories);

        assertThat(actual.getHistories()).containsExactly(deleteHistory1, deleteHistory2, deleteHistory3);
    }
}
