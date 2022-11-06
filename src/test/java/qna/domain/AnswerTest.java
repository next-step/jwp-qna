package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");

    public static final Answer A1() {
        return new Answer(JAVAJIGI, Q1(), "Answers Contents1");
    }

    public static final Answer A2 = new Answer(SANJIGI, Q1(), "Answers Contents2");

    public static final Answer A2() {
        return new Answer(SANJIGI, Q1(), "Answers Contents2");
    }

    @Test
    void 삭제실패_삭제자와_답변자가_다를때() {
        assertThatThrownBy(() -> A1().delete(SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("답변을 삭제할 권한이 없습니다.");
    }

    @Test
    void 삭제성공_deleted_를_true_로_변경() throws Exception {
        Answer answer = A1();
        answer.delete(JAVAJIGI);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 삭제성공_DeleteHistory_반환() throws Exception {
        Answer answer = A1();
        assertThat(answer.delete(JAVAJIGI)).isEqualTo(new DeleteHistory(ANSWER, answer.getId(), JAVAJIGI));
    }

}
