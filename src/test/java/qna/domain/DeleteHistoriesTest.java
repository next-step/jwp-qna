package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.fixtures.UserTestFixture;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoriesTest {

    private Question question;
    private Answer answer;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTestFixture.손상훈, "title", "contents");
        answer = new Answer(1L, UserTestFixture.손상훈, question, "contents");
        answer2 = new Answer(2L, UserTestFixture.손상훈, question, "contents");
    }


    @Test
    @DisplayName("삭제 이력을 추가한다")
    void add_delete_history_test() {
        // given
        DeleteHistories deleteHistories = new DeleteHistories();

        // when
        deleteHistories.add(DeleteHistory.ofAnswer(answer));

        // then
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("다른 삭제 이력을 모두 추가한다")
    void add_all_delete_history_test() {
        // given
        DeleteHistories questionDeleteHistories = new DeleteHistories();
        questionDeleteHistories.add(DeleteHistory.ofQuestion(question));

        DeleteHistories answerDeleteHistories = new DeleteHistories();
        answerDeleteHistories.add(DeleteHistory.ofAnswer(answer));
        answerDeleteHistories.add(DeleteHistory.ofAnswer(answer2));

        // when
        questionDeleteHistories.addAll(answerDeleteHistories);

        // then
        assertThat(questionDeleteHistories.size()).isEqualTo(3);
    }
}
