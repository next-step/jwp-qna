package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {
    private DeleteHistory questionDeleteHistory;
    private DeleteHistory answerDeleteHistory;

    @BeforeEach
    void before() {
        User writer = new User(1L, "user1", "password", "name", "user1@com");

        questionDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, writer, LocalDateTime.now());
        answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, writer, LocalDateTime.now());
    }

    @Test
    void 질문과_답변_삭제_기록을_저장한다() {
        // given
        DeleteHistories deleteHistories = new DeleteHistories();

        deleteHistories.addHistory(questionDeleteHistory);
        deleteHistories.addHistory(answerDeleteHistory);
        // when
        List<DeleteHistory> histories = deleteHistories.elements();
        // then
        assertThat(histories).hasSize(2);
    }
}