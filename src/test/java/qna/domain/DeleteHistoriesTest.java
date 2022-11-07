package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {

    @Test
    void 삭제_이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer1 = new Answer(1L, writer, question, "삭제 이력 추가 테스트");
        Answer answer2 = new Answer(2L, writer, question, "삭제 이력 추가 테스트");
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        DeleteHistories deleteHistories = answers.delete(writer);
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question.getId(), question.getWriter());

        //when
        deleteHistories.add(deleteHistory);

        //then
        assertAll(
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).containsAll(
                        deleteHistories.unmodifiedDeleteHistories()),
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).contains(
                        deleteHistory),
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).hasSize(3)
        );
    }

    @Test
    void 삭제_이력_순서_테스트() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = new Answer(writer, question, "삭제 이력 추가 테스트");
        DeleteHistory deleteHistory1 = DeleteHistory.ofQuestion(question.getId(), question.getWriter());
        DeleteHistory deleteHistory2 = DeleteHistory.ofAnswer(answer.getId(), answer.getWriter());

        //when
        DeleteHistories deleteHistories1 = new DeleteHistories(Arrays.asList(deleteHistory1, deleteHistory2));
        DeleteHistories deleteHistories2 = new DeleteHistories(Arrays.asList(deleteHistory2, deleteHistory1));
        DeleteHistories deleteHistories3 = new DeleteHistories(Arrays.asList(deleteHistory2, deleteHistory1, deleteHistory1));

        //then
        assertThat(deleteHistories1).isEqualTo(deleteHistories2);
        assertThat(deleteHistories2).isNotEqualTo(deleteHistories3);
    }
}
