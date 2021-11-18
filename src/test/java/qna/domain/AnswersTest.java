package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {

    @Test
    void 단건_저장_확인() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        Question question = new Question("title", "content").writeBy(user);
        Answer firstAnswer = new Answer(user, question, "answer");

        // when
        Answers expectedAnswers = new Answers();
        expectedAnswers.addAnswer(firstAnswer);

        // then
        assertThat(expectedAnswers.equals(new Answers(firstAnswer))).isTrue();
    }

    @Test
    void 다건_저장_확인() {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        Question question = new Question("title", "content").writeBy(user);
        Answer firstAnswer = new Answer(user, question, "answer");
        Answer secondAnswer = new Answer(user, question, "answer");
        Answer thirdAnswer = new Answer(user, question, "answer");
        List<Answer> answers = asList(firstAnswer, secondAnswer, thirdAnswer);

        // when
        Answers expectedAnswers = new Answers();
        expectedAnswers.addAnswers(answers, question);

        // then
        assertThat(expectedAnswers.equals(new Answers(answers))).isTrue();
    }

    @Test
    void 삭제_히스토리_생성() throws CannotDeleteException {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        Question question = new Question("title", "contents").writeBy(user);
        Answers answers = new Answers(new Answer(user, question, "contents"), new Answer(user, question, "contents"));

        // when
        List<DeleteHistory> delete = answers.delete(user);

        // then
        List<DeleteHistory> deleteHistories = asList(new DeleteHistory(ANSWER, question, user, LocalDateTime.now()),
                                                     new DeleteHistory(ANSWER, question, user, LocalDateTime.now())
        );

        assertThat(delete).isEqualTo(deleteHistories);
    }

}
