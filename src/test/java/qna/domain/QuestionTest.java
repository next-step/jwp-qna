package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Test
    void 내가_작성한_질문이_아닌경우_질문을_삭제할_수_없다() {
        Question question = Q1.writeBy(UserTest.JAVAJIGI_WITH_ID);
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI_WITH_ID))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_삭제시_삭제_이력을_반환한다() throws CannotDeleteException {
        User user = UserTest.JAVAJIGI_WITH_ID;
        Question question = Q1.writeBy(user);
        DeleteHistory history = question.delete(user);
        assertThat(question.isDeleted()).isTrue();
        assertThat(history.getDeleteByUser()).isEqualTo(user);
        assertThat(history.getContentId()).isEqualTo(question.getId());
    }
}
