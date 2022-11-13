package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("Answer 등록시 Question에 등록된 Answer 조회 테스트")
    public void getAnswers() {
        Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThat(Q1.getAnswers().size()).isEqualTo(1);
        assertThat(Q1.getAnswers()).containsExactly(A1);

        Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
        assertThat(Q1.getAnswers().size()).isEqualTo(2);
        assertThat(Q1.getAnswers()).containsExactly(A1, A2);
    }

    @Test
    @DisplayName("로그인한 사용자의 것이 아닌 질문글을 삭제할 때 validate 오류")
    public void validateDelete() {
        assertThatThrownBy(() -> {
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("삭제 성공")
    public void delete() throws CannotDeleteException {
        new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents1");
        DeleteHistories deleteHistories = Q2.delete(UserTest.SANJIGI);

        assertThat(deleteHistories).isNotNull();
        assertThat(deleteHistories.get().size()).isEqualTo(2);
    }
}
