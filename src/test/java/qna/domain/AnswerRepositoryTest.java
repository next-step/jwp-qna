package qna.domain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.AnswerTest.A1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    public void answer_to_question_save_테스트() {
        User user = users.save(JAVAJIGI);
        Question question = questions.save(Q1);
        A1.toQuestion(question);
        Answer actual = answers.save(A1);
        assertThat(actual.getContents()).isEqualTo("Answers Contents1");
    }

    @Test
    public void isOwner_테스트() {
        User user = new User("chajs226", "1q2w3e", "차준성", "chajs226@gmail.com");
        Question question = new Question("qeustionsTitle", "questionContents").writeBy(user);
        Question question_saved = questions.save(question);
        Answer answer = new Answer(user, question, "contents");
        answer.toQuestion(question);
        Answer actual = answers.save(answer);
        assertThat(actual.isOwner(user)).isTrue();
    }

    @Test
    public void delete_삭제권한_다른사람답변존재_오류() {
/*
        User user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        users.save(user1);
*//*
        Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question question_saved = questions.save(question);
        *//*User user2 = new User("chajs226", "password", "name", "chajs226@gmail.com");
        users.save(user2);*//*
        Answer answer = new Answer(UserTest.JUNSEONG, question_saved, "contents");
        answer.toQuestion(question);
        Answer answer_saved = answers.save(answer);

        assertThatThrownBy(() -> answer_saved.findAnswersForDeleteWithSameWriterAuth(JAVAJIGI, question_saved.getId()))
                .isInstanceOf(CannotDeleteException.class);*/
    }
}