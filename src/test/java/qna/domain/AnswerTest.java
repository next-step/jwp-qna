package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("작성자가 아닌 사용자는 답변을 삭제할 수 없다.")
    @Test
    void deleteFailTest() {
        //when & then
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class)
                                                            .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("삭제하면 답변이 삭제 상태가 되고, 삭제 내역을 반환한다.")
    @Test
    void deleteSuccessTest() {
        //when
        DeleteHistory delete = A2.delete(UserTest.SANJIGI);
        //then
        assertAll(
            () -> assertThat(A2.isDeleted()).isTrue(),
            () -> assertThat(delete).isEqualTo(new DeleteHistory(ContentType.ANSWER, A2.getId(), A2.getWriter(), LocalDateTime.now()))
        );
    }
}
