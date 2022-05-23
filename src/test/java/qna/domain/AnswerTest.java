package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("로그인 사용자와 답변한 사람이 같지 않으면 에러를 반환한다.")
    void no_owner_delete_answer_test() {
        assertThatThrownBy(() -> {
            A1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContainingAll("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("답변을 삭제하면 삭제 여부는 true 가 되고, 삭제히스토리를 생성한다.")
    void delete_test() throws CannotDeleteException {
        DeleteHistory actual = A1.delete(UserTest.JAVAJIGI);
        DeleteHistory expected = new DeleteHistory(ContentType.ANSWER,
                A1.getId(),
                A1.getWriter(),
                A1.getUpdatedAt());
        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(A1.isDeleted()).isTrue()
        );
    }
}
