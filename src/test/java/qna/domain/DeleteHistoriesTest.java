package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoriesTest {

    @Test
    @DisplayName("삭제 이력을 추가한다")
    void add_delete_history_test() {
        Answer answer = AnswerTestFixture.createAnswerWithId(1L);
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofAnswer(answer));
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("다른 삭제 이력을 모두 추가한다")
    void add_all_delete_history_test() {
        DeleteHistories questionDeleteHistories = new DeleteHistories();
        Question question = QuestionTestFixture.createQuestionWithId(1L);
        questionDeleteHistories.add(DeleteHistory.ofQuestion(question));

        DeleteHistories answerDeleteHistories = new DeleteHistories();
        Answer answer = AnswerTestFixture.createAnswerWithId(1L);
        answerDeleteHistories.add(DeleteHistory.ofAnswer(answer));
        Answer answer2 = AnswerTestFixture.createAnswerWithId(2L);
        answerDeleteHistories.add(DeleteHistory.ofAnswer(answer2));

        questionDeleteHistories.addAll(answerDeleteHistories);

        assertThat(questionDeleteHistories.size()).isEqualTo(3);
    }
}
