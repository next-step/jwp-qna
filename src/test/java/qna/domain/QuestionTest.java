package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 질문_주인_확인() {
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    void 질문_삭제() throws CannotDeleteException {
        Q1.addAnswer(new Answer(UserTest.JAVAJIGI, Q1, "content"));

        List<DeleteHistory> deleteHistories = Q1.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }

    @Test
    void 질문_삭제_예외() throws CannotDeleteException {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
