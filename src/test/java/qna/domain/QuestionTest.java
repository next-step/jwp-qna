package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question TITLEA_QUESTION = new Question("titleA", "contents1");
    public static final Question TITLEB_QUESTION = new Question("titleB", "contents2");

    @Test
    @DisplayName("로그인 사용자와 질문자가 같은 경우 삭제")
    void delete_question_if_equals() throws CannotDeleteException {
        Question question = new Question("titleA", "contents1").writeBy(UserTest.JAVAJIGI);

        question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자가 질문자와 다를 경우 삭제 오류")
    void delete_question_if_not_equals() {
        Question question = new Question("titleA", "contents1").writeBy(UserTest.JAVAJIGI);

        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }
}
