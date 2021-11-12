package qna.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;

class AnswersTest {

    public static Answers ANSWERS = new Answers(Arrays.asList(A1, A2));

    @Test
    void checkIsOwner_다른_사람이_쓴_답변이_있는_경우_에러를_발생한다() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> ANSWERS.checkIsOwner(JAVAJIGI));
    }

    @Test
    void delete_답변을_삭제한다() {
        ANSWERS.delete();
        assertThat(ANSWERS.getAnswers().get(0).isDeleted()).isTrue();
        assertThat(ANSWERS.getAnswers().get(1).isDeleted()).isTrue();
    }
}