package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void delete_성공() throws CannotDeleteException {

        //given
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        //when
        List<DeleteHistory> actualHistory = answers.delete(UserTest.JAVAJIGI);
        //when
        assertThat(actualHistory).isNotNull();
    }

}
