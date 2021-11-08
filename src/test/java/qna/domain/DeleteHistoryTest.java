package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

public class DeleteHistoryTest {

    @Test
    void question_질문_타입으로_생성한다() {
        DeleteHistory deleteHistory = DeleteHistory.question(1L, JAVAJIGI);
        assertAll(
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(1L),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(JAVAJIGI)
        );
    }

    @Test
    void answer_답변_타입으로_생성한다() {
        DeleteHistory deleteHistory = DeleteHistory.answer(1L, JAVAJIGI);
        assertAll(
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(1L),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(JAVAJIGI)
        );
    }
}
