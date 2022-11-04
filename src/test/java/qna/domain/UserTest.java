package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserTest {

    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    TestEntityManager testEntityManager;

    EntityManager em;

    @BeforeEach
    void setUp() {
        em = testEntityManager.getEntityManager();
    }

    @Test
    void 영속성_컨텍스트_1차_캐시_초기화_동등성_테스트() {
        User user = TestUserFactory.create("javajigi");

        testEntityManager.persistAndFlush(user);
        testEntityManager.clear();

        User findUser = testEntityManager.find(User.class, user.getId());
        assertThat(user).isEqualTo(findUser);
    }

    @Test
    void 준영속_상태_동등성_테스트() {
        User user1 = TestUserFactory.create("javajigi");
        User user2 = TestUserFactory.create("sanjigi");

        assertThat(user1).isNotEqualTo(user2);
    }
}
