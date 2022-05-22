package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

class AnswerTest {
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

    @Test
    @DisplayName("게시자 확인")
    void isNotOwner() {
        assertThat(A1.isNotOwner(UserTest.SANJIGI)).isTrue();
    }

    @Test
    @DisplayName("삭제 - 답변 작성자와 로그인 유저가 다를 때")
    void delete_isNotOwner() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 - 답변 작성자와 로그인 유저가 같을 때")
    void delete_isOwner() {
        assertThatCode(() -> A1.delete(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }
}
