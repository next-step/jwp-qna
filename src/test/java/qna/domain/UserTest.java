package qna.domain;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User( "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    EntityManager em;

    @Test
    public void 엔티티_생성(){
        User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        em.persist(user);
        User findUser = em.createQuery("select u from User u where u.id=:id",User.class)
            .setParameter("id",user.getId())
            .getSingleResult();

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    public void 사용자_패스워드_매칭(){
        String password = "12345678";
        User user = new User("jerry92k", password,"jerrykim","jerry@gmail.com");

        assertThat(user.matchPassword(password)).isTrue();
    }

    @Test
    public void 사용자_계정_비교(){

        User user = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");
        User comparingUser = new User("jerry92k", "12345678","jerrykim","jerry@gmail.com");

        assertThat(user.equalsNameAndEmail(null)).isFalse();
        assertThat(user.equalsNameAndEmail(comparingUser)).isTrue();
    }
}
