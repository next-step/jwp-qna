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

    @Test
    public void add_answer() {
        //given
        Answers answers = new Answers();
        Answer expected = AnswerTest.A1;
        //when
        answers.add(expected);
        //when
        assertThat(answers.contains(expected)).isTrue();
    }

    @Test
    public void remove_answer() {
        //given
        Answers answers = new Answers();
        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A2);
        //when
        answers.remove(AnswerTest.A1);
        //then
        assertThat(answers.contains(AnswerTest.A1)).isFalse();
        assertThat(answers.contains(AnswerTest.A2)).isTrue();
    }

}
