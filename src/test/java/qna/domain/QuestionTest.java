package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @BeforeEach
    private void setUp() {
        Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("질문삭제할때 삭제요청자가 null 일경우에 에러를 던진다.")
    void givenNull_whenDelete_thenThrow() {
        assertThatThrownBy(() -> Q1.delete(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("삭제요청자가 null 입니다.");
    }

    @Test
    @DisplayName("질문삭제할때 삭제요청자가 질문작성자와 다를경우 에러를 던진다.")
    void givenAnotherUser_whenDelete_thenThrow() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("질문 작성자가 아닙니다.");
    }

    @Test
    @DisplayName("삭제요청자, 질문작성자, 답변작성자가 모두 같을때 삭제후 삭제이력리스트를 리턴한다.")
    void givenValidUser_whenDelete_thenSuccess() throws CannotDeleteException {
        Q1.addAnswer(AnswerTest.A1);

        List<DeleteHistory> deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistories).hasSize(2);
    }
}
