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
        Answer answer1 = new Answer(writer, question, "삭제 이력 추가 테스트");
        Answer answer2 = new Answer(writer, question, "삭제 이력 추가 테스트");
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
}
