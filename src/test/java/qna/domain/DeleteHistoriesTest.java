package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {
    private Question question;

    @BeforeEach
    void before() {
        User writer = new User(1L, "user1", "password", "name", "user1@com");

        question = new Question("title1", "contents1").writeBy(writer);
        new Answer(writer, question, "Answers Contents1");
        new Answer(writer, question, "Answers Contents2");
    }

    @Test
    void 질문과_답변_삭제_기록을_저장한다() {
        // given
        DeleteHistories deleteHistories = new DeleteHistories();
        // when
        List<DeleteHistory> histories = deleteHistories.addHistory(question);
        // then
        assertThat(histories).hasSize(3);
    }
}