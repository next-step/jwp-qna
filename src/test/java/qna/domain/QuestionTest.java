package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;
import qna.UnAuthorizedException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    EntityManager em;

    @Test
    public void 엔티티_생성(){
        Question question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        em.persist(question);
        Question findQuestion = em.createQuery("select q from Question q where q.id=:id",Question.class)
            .setParameter("id",question.getId())
            .getSingleResult();

        assertThat(findQuestion).isEqualTo(question);
    }
}
