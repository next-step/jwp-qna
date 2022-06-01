package qna.domain;

import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 작성자_미입력_오류_테스트() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "content"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문_미입력_오류_테스트() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "content"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 질문_작성자_확인() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A2.isOwner(UserTest.JAVAJIGI)).isFalse();
    }
}