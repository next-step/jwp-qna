package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 답변데이터_소프트삭제() {
        // given
        Answer answer = A1;

        // when
        answer.deleteAndReturnDeleteHistory(UserTest.JAVAJIGI);

        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 로그인유저_답변자_다를경우_삭제불가능() {
        assertThatThrownBy(() -> A1.deleteAndReturnDeleteHistory(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void 삭제_이력_반환() {
        // given
        Answer answer = A1;

        // when
        DeleteHistory deleteHistory = answer.deleteAndReturnDeleteHistory(UserTest.JAVAJIGI);

        // then
        assertAll(
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(A1.getId()),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(A1.getWriter())
        );
    }
}
