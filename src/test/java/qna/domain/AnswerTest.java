package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;
import qna.UnAuthorizedException;


@DataJpaTest
public class AnswerTest {

    @Autowired
    EntityManager em;

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    public void 엔티티_생성(){
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answer entity unit test");
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
}
