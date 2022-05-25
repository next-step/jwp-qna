package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

class AnswerTest {
    private User writer;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "id", "password", "이름", "a@b.com");
        question = new Question(1L, "제목", "내용");

        answer = new Answer(1L, writer, question, "답변 내용");
    }

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같은 경우 삭제할 수 있고 삭제이력을 남긴다.")
    void 삭제_성공() throws CannotDeleteException {
        DeleteHistory deleteHistory = answer.delete(writer);
        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory).isNotNull()
        );

    }

    @Test
    @DisplayName("로그인 사용자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void 삭제_실패() {
        User user = new User(2L, "user", "password1", "유저", "user@b.com");
        assertThatThrownBy(() -> answer.delete(user)).isInstanceOf(CannotDeleteException.class);
    }
}
