package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    void delete_질문을_삭제한다() throws CannotDeleteException {
        Q1.addAnswer(A1);
        List<DeleteHistory> histories = Q1.delete(JAVAJIGI);
        assertAll(
            () -> assertThat(histories.size()).isEqualTo(2),
            () -> assertThat(Q1.isDeleted()).isTrue()
        );
    }
}
