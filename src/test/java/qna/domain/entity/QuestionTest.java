package qna.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.ArrayList;

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
    @DisplayName("답변을 가질 경우 에러를 발생시키는 테스트")
    void has_exist_answer_delete_test() {
        User user = UserTest.JAVAJIGI;
        Question expected = Q1;
        ArrayList answers = new ArrayList();

        answers.add(AnswerTest.A1);
        expected.setAnswers(answers);

        assertThatThrownBy(() -> Q1.delete(user))
                .isInstanceOf(CannotDeleteException.class).hasMessage("답변이 달려있으므로, 삭제할수 없습니다.");
    }

    @Test
    @DisplayName("삭제가 성공해서, 삭제 상태값이 변경되는 테스트")
    void success_delete_test() throws CannotDeleteException {
        Question expected = QuestionTest.Q1;

        expected.delete(UserTest.JAVAJIGI);

        assertThat(expected.getDeleted()).isTrue();
    }
}
