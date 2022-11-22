package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.constant.DeleteErrorMessage;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    Answer answer;
    User writer;

    @BeforeEach
    public void setUp() {
        answer = AnswerTest.A1;
        writer = UserTest.JAVAJIGI;
    }

    @Test
    @DisplayName("로그인 사용자와 답변자가 다르면 삭제 불가능")
    void validateDelete() {
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(DeleteErrorMessage.NOT_HAVE_PERMISSION_DELETE_ANSWER);
    }


    @Test
    @DisplayName("삭제 성공, 상태값 변경 확인")
    void deleteSuccess() throws CannotDeleteException {

        A1.delete(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 성공 후 히스토리 등록")
    void deleteSuccessAndSaveDeleteHistory() throws CannotDeleteException {
        DeleteHistory deleteHistory = answer.delete(writer);

        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), writer, LocalDateTime.now()));

    }

}
