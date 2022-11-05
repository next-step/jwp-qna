package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("Id가 null 일 경우 em.persist()를 호출하여 영속성 컨텍스트에 저장한다")
    void save_호출시_영속시킴() {
        User expected = new User("writer", "1111", "작성자", "writer@naver.com");
        User actual = users.save(expected);
        actual.setId(2L);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("Id가 이미 존재 할 경우 merge()가 호출되면서 select 쿼리 발생, 동등성과 동일성이 보장되지 않는다")
    void merge() {
        User expected = UserTest.JAVAJIGI;
        User actual = users.save(expected);
        actual.setId(2L);
        assertThat(actual.getId()).isNotEqualTo(expected.getId());
        assertThat(actual == expected).isFalse();
    }

    @Test
    @DisplayName("delete() 는 Id 값으로 delete 쿼리 수행")
    void delete() {
        User expected = UserTest.JAVAJIGI;
        User actual = users.save(expected);
        users.delete(actual);
        users.flush();
    }

    @Test
    @DisplayName("메서드 이름으로 쿼리를 생성할 수 있으며, Id 값이 아니기 때문에 select 쿼리 발생")
    void findByUserId() {
        String expected = "writer";
        User makeUser = new User(expected, "1111", "작성자", "writer@naver.com");
        users.save(makeUser);
        users.findById(1L); // 1차 캐시에서 꺼내옴
        String actual = users.findByUserId(expected).get().getUserId();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("메서드 이름으로 카운트 쿼리 생성")
    void countByName() {
        User makeUser = UserTest.JAVAJIGI;
        User makeUser2 = UserTest.SANJIGI;
        users.save(makeUser);
        users.save(makeUser2);
        assertThat(users.countByName("name")).isEqualTo(2);
    }

    @ParameterizedTest
    @DisplayName("변경을 감지하여 update 쿼리가 수행된다")
    @CsvSource(value = "reader:2222:열람자:reader@naver.com", delimiter = ':')
    void update(String userId, String password, String name, String email) {
        User user1 = users.save(UserTest.JAVAJIGI);
        user1.setUserId(userId);
        user1.setPassword(password);
        user1.setName(name);
        user1.setEmail(email);
        flushAndClear();
        User user2 = users.findById(user1.getId()).get();
        assertThat(user2.getUserId()).isEqualTo(userId);
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
