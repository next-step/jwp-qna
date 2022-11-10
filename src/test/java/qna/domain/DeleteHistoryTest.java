package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static qna.domain.AnswerTest.ANSWER_1;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.UserTest.JAVAJIGI;

@DisplayName("삭제 내역")
public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_HISTORY_ANSWER = new DeleteHistory(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI.getId(), LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_QUESTION = new DeleteHistory(ContentType.QUESTION, QUESTION_1.getId(), QUESTION_1.getWriterId(), LocalDateTime.now());

    @DisplayName("삭제 내역 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new DeleteHistory(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI.getId(), LocalDateTime.now()));
    }
}
