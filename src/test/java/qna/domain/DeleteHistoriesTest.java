package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswersTest.ANSWERS;
import static qna.domain.UserTest.JAVAJIGI;

class DeleteHistoriesTest {

    @Test
    void delete_질문과_답변을_삭제_히스토리에_저장한다() {
        // given
        DeleteHistories deleteHistories = new DeleteHistories(ANSWERS);
        DeleteHistory questionHistory = DeleteHistory.question(1L, JAVAJIGI);

        // when
        List<DeleteHistory> expected = deleteHistories.delete(questionHistory);

        // then
        assertThat(expected.size()).isEqualTo(3);
    }
}