package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;


@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    EntityManager em;

    @Test
    public void 엔티티_생성(){
        User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        em.persist(user);
        Question question = new Question("title1", "contents1").writeBy(user);
        em.persist(question);

        Answer answer = new Answer(user,question, "Answer entity unit test");
        em.persist(answer);
        Answer findAnswer = em.createQuery("select a from Answer a where a.id=:id",Answer.class)
            .setParameter("id",answer.getId())
            .getSingleResult();
        assertThat(findAnswer).isEqualTo(answer);
    }

    @Test
    public void 엔티티_생성_User_객체_NUll_예외(){
        assertThatThrownBy(()->{
            Answer answer = new Answer(null, QuestionTest.Q1, "Answer entity unit test");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    public void 엔티티_생성_Question_객체_NUll_예외(){
        assertThatThrownBy(()->{
            Answer answer = new Answer(UserTest.JAVAJIGI, null, "Answer entity unit test");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void delete_작성자와_삭제요청자가_다른_경우_예외(){
        User writer = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        User deleter = new User("tom", "34323f","tom","tom@gmail.com");
        em.persist(writer);
        em.persist(deleter);

        Answer answer = new Answer(deleter, QuestionTest.Q1, "Answer entity unit test");

        assertThatThrownBy(()->{
            answer.delete(writer);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    public void delete_작성자와_삭제요청자가_다르지만_이미_삭제된_경우_정상_처리() throws CannotDeleteException {
        User writer = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        User deleter = new User("tom", "34323f","tom","tom@gmail.com");
        em.persist(writer);
        em.persist(deleter);

        Answer answer = new Answer(writer, QuestionTest.Q1, "Answer entity unit test");
        answer.delete(writer);
        answer.delete(deleter);
        assertThat(answer.isDeleted()).isTrue();
    }
}
