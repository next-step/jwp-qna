package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("생성자 실패 케이스")
    void Answer_create_fail() {
        assertThatExceptionOfType(UnAuthorizedException.class)
            .isThrownBy(() -> new Answer(null, QuestionTest.Q1, "test"));
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "test"));
    }

    @Test
    @DisplayName("생성자 테스트")
    void Answer_create() {
        assertThat(A1).isEqualTo(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
    }

    @Test
    @DisplayName("작성자가 본인인지 확인")
    void Answer_isOwner() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("Question 변경 확인")
    void Answer_toQuestion() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.toQuestion(QuestionTest.Q2);
        assertThat(answer.getQuestion()).isEqualTo(QuestionTest.Q2);
    }

    @Test
    @DisplayName("Answer 삭제시 권한 체크")
    void Answer_delete_fail() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> A1.delete(UserTest.SANJIGI));
    }

    @Test
    @DisplayName("Answer 삭제시 성공")
    void Answer_delete_success() {
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistory)
            .isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), UserTest.JAVAJIGI, LocalDateTime.now()));
    }

}
