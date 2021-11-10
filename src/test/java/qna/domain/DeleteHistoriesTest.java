package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswersTest.ANSWERS;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class DeleteHistoriesTest {

    @Test
    void delete_질문과_답변을_삭제_히스토리에_저장한다() {
        // given
        DeleteHistories deleteHistories = new DeleteHistories();
        DeleteHistory questionHistory = DeleteHistory.question(1L, JAVAJIGI);

        // when
        DeleteHistories expected = deleteHistories.delete(ANSWERS, questionHistory);

        // then
        assertThat(expected).isEqualTo(new DeleteHistories(Arrays.asList(
                questionHistory,
                DeleteHistory.answer(null, JAVAJIGI),
                DeleteHistory.answer(null, SANJIGI)))
        );
    }
}