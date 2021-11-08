package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

    @Test
    void checkIsOwner_다른_사람이_쓴_글이면_에러를_발생한다() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.checkOwner(SANJIGI));
    }
}
