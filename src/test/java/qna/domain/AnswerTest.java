package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("답변 삭제 할때 삭제 상태로 변경해준다.")
    @Test
    void deleteTest() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 DeleteHistory에 이력정보객체 생성.")
    @Test
    void deleteHistoryToAnswer() throws CannotDeleteException {
        final DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistory).isNotNull();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, A1.getId(), UserTest.JAVAJIGI));
    }

    @DisplayName("답변 등록자가 삭제하려는 사람과 같을때 삭제 되는지 검증")
    @Test
    void deleteAnswerByUser() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);

        assertThat(A1.isDeleted()).isTrue();
    }

    @DisplayName("답변 등록자가 삭제하려는 사람과 다를때 에러")
    @Test
    void deleteAnswerByUserError() throws CannotDeleteException {
        assertThatThrownBy(() -> {
            A1.delete(new User("lsm", "password", "이승민", "test@test.com"));
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
