package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문이 삭제되어 삭처여부가 true인지 확인 테스트")
    @Test
    public void deleteFlagTest() throws CannotDeleteException {
        Q1.delete(UserTest.JAVAJIGI);
        assertTrue(Q1.isDeleted());
    }

    @DisplayName("작성자와 요청자가 다를때 삭제예외 테스트")
    @Test
    public void whenAnotherUserDeleteQuestion() {
        assertThatThrownBy(() -> {
            Q1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("삭제이력이 추가되는지 확인 테스트")
    @Test
    public void deleteHistory() throws CannotDeleteException {
        DeleteHistories deleteHistories = Q1.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories.getDeleteHistories()).hasSize(1);
    }

}
