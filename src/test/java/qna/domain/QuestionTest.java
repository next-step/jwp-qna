package qna.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void delete_성공() {
        List<DeleteHistory> deleteHistories = Q1.delete(UserTest.JAVAJIGI);

        Assertions.assertAll(
                () -> assertThat(Q1.isDeleted()).isTrue(),
                () -> assertThat(deleteHistories).containsExactly(new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter()))
        );
    }

    @Test
    void delete_다른사람이_쓴_답변() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
