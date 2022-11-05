package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {

    @Test
    void 질문_삭제이력_추가() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question = TestQuestionFactory.create(writer);

        //when
        DeleteHistories deleteHistories = question.delete(writer);
        DeleteHistory expectDeleteHistory = DeleteHistory.createDeleteHistory(ContentType.QUESTION, question.getId(), writer);

        //then
        assertThat(deleteHistories.unmodifiedDeleteHistories()).contains(expectDeleteHistory);
    }

    @Test
    void 삭제이력_두_개_합치기() {
        //given
        User writer = TestUserFactory.create("javajigi");
        Question question1 = TestQuestionFactory.create(writer);
        Question question2 = TestQuestionFactory.create(writer);
        DeleteHistories deleteHistories1 = question1.delete(writer);
        DeleteHistories deleteHistories2 = question2.delete(writer);

        //when
        DeleteHistories deleteHistories = deleteHistories1.merge(deleteHistories2);

        //then
        assertAll(
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).containsAll(
                        deleteHistories1.unmodifiedDeleteHistories()),
                () -> assertThat(deleteHistories.unmodifiedDeleteHistories()).containsAll(
                        deleteHistories2.unmodifiedDeleteHistories())
        );
    }
}
