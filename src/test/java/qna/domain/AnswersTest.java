package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;

class AnswersTest {

    @Test
    void checkIsOwner_다른_사람이_쓴_답변이_있는_경우_에러를_발생한다() {
        Answers answers = new Answers(Arrays.asList(A1, A2));
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answers.checkIsOwner(JAVAJIGI));
    }
}