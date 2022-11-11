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
    public static final DeleteHistory DELETE_HISTORY_ANSWER = DeleteHistory.of(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory DELETE_HISTORY_QUESTION = DeleteHistory.of(ContentType.QUESTION, QUESTION_1.getId(), QUESTION_1.getWriter(), LocalDateTime.now());

    @DisplayName("삭제 내역 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> DeleteHistory.of(ContentType.ANSWER, ANSWER_1.getId(), JAVAJIGI, LocalDateTime.now()));
    }
}
