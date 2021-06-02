package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 생성")
    public void createAnswer() {
        User user = UserTest.JAVAJIGI;
        Question question = QuestionTest.Q1;
        String contents = "Answers Contents1";
        Answer answer = new Answer(user, question, contents);

        assertThat(answer.equals(new Answer(user, question, contents))).isTrue();
    }

    @Test
    @DisplayName("답변 작성자 확인")
    public void answerUserCheck(){
        assertThatThrownBy(
                () -> new Answer(null, QuestionTest.Q1, "Answers Contents3")
        ).isInstanceOf(UnAuthorizedException.class).hasMessageContaining("답변은 로그인 하신 후에 작성 가능합니다.");
    }

    @Test
    @DisplayName("답변할 질문 확인")
    public void answerQuestionCheck(){
        assertThatThrownBy(
                () -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents3")
        ).isInstanceOf(NotFoundException.class).hasMessageContaining("답변을 할 질문이 없습니다.");
    }

    @Test
    @DisplayName("답변 삭제")
    public void answerDelete() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 이력")
    public void answerDeleteHistory() throws CannotDeleteException {
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistory.equals(DeleteHistory.answerHistory(AnswerTest.A1.getId(), UserTest.JAVAJIGI))).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 권한 없음")
    public void answerDeleteNoAuth() {
        assertThatThrownBy(
                () -> A1.delete(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class).hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
