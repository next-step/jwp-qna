package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 다른유저_질문_삭제시_에러() {
        Question question = Q2.writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> question.deleteWithAnswers(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_삭제시_이력을_반환한다() throws CannotDeleteException {
        User user = UserTest.JAVAJIGI;
        Question question = Q1.writeBy(user);
        List<DeleteHistory> history = question.deleteWithAnswers(user);
        assertThat(question.isDeleted()).isTrue();
        assertThat(history.get(0).getId()).isEqualTo(question.getId());
    }
}
