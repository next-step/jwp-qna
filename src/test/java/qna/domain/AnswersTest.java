package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswersTest {

    @Test
    public void delete_다른_사람이_쓴_답변() {
        //given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        //when then
        assertThatThrownBy(() -> answers.delete(UserTest.NEWJIGI)).isInstanceOf(CannotDeleteException.class);
    }


}
