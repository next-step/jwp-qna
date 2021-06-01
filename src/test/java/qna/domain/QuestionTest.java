package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);

    @Test
    void 질문데이터_소프트삭제() {
        // given
        Question question = Q1;

        // when
        question.deleteAndReturnDeleteHistory(UserTest.JAVAJIGI);

        // then
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 로그인사용자_writer_다를경우_삭제불가능() {
        assertThatThrownBy(() -> Q1.deleteAndReturnDeleteHistory(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 삭제_이력_반환() {
        // given
        Question question = Q1;

        // when
        DeleteHistory deleteHistory = question.deleteAndReturnDeleteHistory(UserTest.JAVAJIGI);

        // then
        assertThat(deleteHistory.getContentId()).isEqualTo(question.getId());
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
        assertThat(deleteHistory.getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
    }
}
