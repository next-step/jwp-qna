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
    @DisplayName("질문 삭제 정상 동작 검증")
    void delete() throws CannotDeleteException {
        QuestionTest.Q1.delete(UserTest.JAVAJIGI);
        assertThat(QuestionTest.Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 삭제시 로그인 유저가 다를 경우 예외 발생")
    void deleteException() {
        assertThatThrownBy(() -> QuestionTest.Q1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
