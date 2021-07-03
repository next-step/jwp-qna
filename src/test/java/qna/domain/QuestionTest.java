package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("로그인 사용자와 질문 작성자가 같을 경우 삭제")
    @Test
    void deleteBySameUser() throws CannotDeleteException {
        User loginUser = UserTest.JAVAJIGI;
        Q1.delete(loginUser);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @DisplayName("로그인 사용자와 질문 작성자가 다를 경우 예외 발생")
    @Test
    void deleteByWrongUser() {
        User loginUser = UserTest.SANJIGI;
        assertThatThrownBy(() -> Q1.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

}
