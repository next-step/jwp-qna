package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("질문 생성 - 빈 작성자")
    void create_emptyWriter() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents1"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("질문 생성 - 빈 질문")
    void create_emptyQuestion() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("게시자 확인")
    void isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }
}
