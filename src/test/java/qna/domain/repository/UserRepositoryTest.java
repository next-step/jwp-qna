package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository users;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("Id가 null 일 경우 em.persist()를 호출하여 영속성 컨텍스트에 저장한다")
    void save_호출시_영속시킴() {
        User loginUser = new User("writer", "1111", "작성자", "writer@naver.com");
        User target = new User("writer", "1111", "작성자2", "writer2@naver.com");
        User actual = users.save(loginUser);
        actual.update(loginUser, target);
        assertThat(actual.getId()).isEqualTo(loginUser.getId());
        assertThat(actual == loginUser).isTrue();
        assertThat(loginUser.equalsNameAndEmail(target)).isTrue();
    }

    @Test
    @DisplayName("Id가 이미 존재 할 경우 merge()가 호출되면서 select 쿼리 발생, 동등성과 동일성이 보장되지 않는다")
    void merge() {
        User loginUser = new User(1L, "writer", "1111", "작성자", "writer@naver.com");
        User target = new User("writer", "1111", "작성자2", "writer2@naver.com");
        User actual = users.save(loginUser);
        actual.update(loginUser, target);
        assertThat(loginUser.equalsNameAndEmail(actual)).isFalse();
    }

    @Test
    @DisplayName("delete() 는 Id 값으로 delete 쿼리 수행")
    void delete() {
        User expected = new User("writer", "1111", "작성자", "writer@naver.com");
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
        User writer = new User("writer", "1111", "작성자", "writer@naver.com");
        User writer2 = new User("writer2", "2222", "작성자", "writer2@naver.com");
        users.save(writer);
        users.save(writer2);
        assertThat(users.countByName("작성자")).isEqualTo(2);
    }

    @Test
    @DisplayName("변경을 감지하여 update 쿼리가 수행된다")
    void update() {
        User loginUser = users.save(new User("writer", "1111", "작성자", "writer@naver.com"));
        User target = new User("writer", "1111", "작성자2", "writer2@naver.com");
        loginUser.update(loginUser, target);
        flushAndClear();
        User findLoginUser = users.findById(loginUser.getId()).get();
        assertThat(findLoginUser.getUserId()).isEqualTo(loginUser.getUserId());
    }

    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
