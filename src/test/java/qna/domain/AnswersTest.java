package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    private Answers answers;
    @Test
    void delete()  {
        // given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        Answer answer1 = new Answer(questionUser, question, "Answers Contents1");

        answers = new Answers();
        answers.add(answer1);

        // when
        List<DeleteHistory> deleteHistories = null;
        try {
            deleteHistories = answers.delete(questionUser);
        } catch (CannotDeleteException e) {
            e.printStackTrace();
        }
        // then
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @Test
    void deleteOtherUser() {
        //given
        User questionUser = new User("user1", "user1Pass", "User1", "user1@gmail.com");
        Question question = new Question("Question1 title", "Question1 contents").writeBy(questionUser);
        User answerUser = new User("user2", "user2Pass", "User2", "user2@gmail.com");
        Answer answer1 = new Answer(questionUser, question, "Answers Contents1");
        Answer answer2 = new Answer(answerUser, question, "Answers Contents2");

        answers = new Answers();
        answers.add(answer1);
        answers.add(answer2);

        //when

        //then
        assertThatThrownBy(()-> {
            answers.delete(questionUser);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
