package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoryTest {

    @Test
    void 질문_및_삭제_DeleteHistory_생성() {
        User user = new User(1L, "mwkown", "password", "권민욱", "mwkwon@github.com");
        Question question = new Question(1L, "title", "contents").writeBy(user);
        Answer answer = new Answer(1L, user, question, "contents");
        question.addAnswer(answer);

        List<DeleteHistory> deleteHistories = DeleteHistory.createDeleteHistories(question);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }

    @Test
    void 질문만_존재할_경우_DeleteHistory_생성() {
        User user = new User(1L, "mwkown", "password", "권민욱", "mwkwon@github.com");
        Question question = new Question(1L, "title", "contents").writeBy(user);

        List<DeleteHistory> deleteHistories = DeleteHistory.createDeleteHistories(question);
        assertThat(deleteHistories.size()).isEqualTo(1);
    }
}
