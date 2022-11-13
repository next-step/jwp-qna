package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("삭제 내역들")
class DeleteHistoriesTest {

    @DisplayName("삭제 내역을 추가한다.")
    @Test
    void add() {

        User javajigi = UserTest.JAVAJIGI_ID;

        Question question = QuestionTest.QUESTION_1_ID;
        question.addAnswer(AnswerTest.ANSWER_1_ID);
        question.addAnswer(AnswerTest.ANSWER_2_ID);

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(question.getId(), javajigi));

        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @DisplayName("삭제 내역을 리스트를 추가한다.")
    @Test
    void addAll() {

        User javajigi = UserTest.JAVAJIGI_ID;

        Question question = QuestionTest.QUESTION_1_ID;
        question.addAnswer(AnswerTest.ANSWER_1_ID);
        question.addAnswer(AnswerTest.ANSWER_2_ID);

        Answer answer1 = AnswerTest.ANSWER_1_ID;
        Answer answer2 = AnswerTest.ANSWER_2_ID;

        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.addAll(Arrays.asList(DeleteHistory.ofAnswer(answer1.getId(), javajigi), DeleteHistory.ofAnswer(answer2.getId(), javajigi)));

        assertThat(deleteHistories.size()).isEqualTo(2);
    }
}
