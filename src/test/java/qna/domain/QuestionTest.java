package qna.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @ParameterizedTest
    @NullAndEmptySource
    void title_null_테스트(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Question(title, "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @Test
    void 다른_사람이_작성한_질문_삭제_테스트() {
        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> Q1.delete(UserTest.SANJIGI));
    }
}
