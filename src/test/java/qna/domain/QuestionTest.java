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
    @DisplayName("작성자와 다른 사용자가 삭제시 예외가 발생한다")
    void deleteNotOwnerExceptionTest() {
        assertThatThrownBy(() -> Q1.deleteByOwner(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 시 deleted : true 변경, contentType : Question 인 DeleteHistory 를 반환한다")
    void deleteTest() throws CannotDeleteException {
        DeleteHistory deleteHistory = Q1.deleteByOwner(UserTest.JAVAJIGI);

        assertThat(Q1.isDeleted()).isEqualTo(true);
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
    }
}
