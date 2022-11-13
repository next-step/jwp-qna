package qna.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @BeforeEach
    void setUp() {
        Q1.getAnswers().clear();
    }

    @Test
    @DisplayName("로그인한 유저가 글을 작성한 유저가 아닌 경우")
    void not_equal_login_user_and_writer_user_delete_test() {
        User expected = UserTest.SANJIGI;

        assertThatThrownBy(() -> Q1.delete(expected))
                .isInstanceOf(CannotDeleteException.class).hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("삭제가 성공해서, 삭제 상태값이 변경되는 테스트")
    void success_question_delete_test() throws CannotDeleteException {
        Question expected = QuestionTest.Q1;

        expected.delete(UserTest.JAVAJIGI);

        assertThat(expected.getDeleted()).isTrue();
    }
}
