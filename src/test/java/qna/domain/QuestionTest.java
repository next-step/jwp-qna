package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Owner 확인")
    @Test
    void isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue();
    }

    @DisplayName("질문자는 질문을 삭제할 수 있다.")
    @Test
    void valid_delete() throws CannotDeleteException {
        Q1.delete(UserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @DisplayName("질문자가 아니면 질문을 삭제할 수 없다.")
    @Test
    void invalid_delete() {
        assertThatThrownBy(() -> Q2.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }


}
