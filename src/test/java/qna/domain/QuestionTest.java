package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    EntityManager em;

    @Test
    public void 엔티티_생성(){
        User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        em.persist(user);
        Question question = new Question("title1", "contents1").writeBy(user);
        em.persist(question);
        Question findQuestion = em.createQuery("select q from Question q where q.id=:id",Question.class)
            .setParameter("id",question.getId())
            .getSingleResult();

        assertThat(findQuestion).isEqualTo(question);
    }

    @Test
    @DisplayName("question 작성자와 삭제요청자가 다른 경우 예외")
    public void delete_예외테스트(){
        User writer = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        User deleter = new User("tom", "34323f","tom","tom@gmail.com");
        em.persist(writer);
        em.persist(deleter);
        Question question = new Question("title1", "contents1").writeBy(writer);
        em.persist(question);
        assertThatThrownBy(()->{
            question.delete(deleter);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
