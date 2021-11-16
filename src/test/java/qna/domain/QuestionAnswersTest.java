package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionAnswersTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @AfterEach
    void tearDown() {
        answers.deleteAll();
        questions.deleteAll();
        users.deleteAll();
    }

    @Test
    void deleteBy() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questions.save(new Question(user, "title1", "contents1"));
        Answer answer1 = answers.save(new Answer(user, "Answers Contents1"));
        Answer answer2 = answers.save(new Answer(user, "Answers Contents2"));
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        List<DeleteHistory> deleteHistories = question.getAnswers().deleteBy(user);

        assertThat(deleteHistories).contains(
            new DeleteHistory(ANSWER, answer1.getId(), user),
            new DeleteHistory(ANSWER, answer2.getId(), user)
        );
        assertThat(question.getAnswers().allMatch(Answer::isDeleted)).isTrue();
    }

    @Test
    void deleteByInvalidUser() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        Question question = questions.save(new Question(user, "title1", "contents1"));
        User invalidUser = users.save(new User("minseoklim", "1234", "임민석", "mslim@slipp.net"));
        Answer answer = answers.save(new Answer(user, "Answers Contents1"));
        question.addAnswer(answer);
        question.addAnswer(answers.save(new Answer(invalidUser, "Answers Contents2")));


        assertThatThrownBy(() -> question.getAnswers().deleteBy(invalidUser))
            .isInstanceOf(CannotDeleteException.class);
        assertThat(answer.isDeleted()).isFalse();
    }
}
