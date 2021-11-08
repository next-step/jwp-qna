package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    void checkIsOwner_다른_사람이_쓴_글이면_에러를_발생한다() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.checkOwner(SANJIGI));
    }

    @Test
    void delete_질문을_삭제한다() {
        List<DeleteHistory> histories = Q1.delete(Arrays.asList(A1,A2));
        assertAll(
            () -> assertThat(histories.size()).isEqualTo(3),
            () -> assertThat(Q1.isDeleted()).isTrue()
        );
    }
}
