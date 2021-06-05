package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.wrappers.UserName;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 저장")
    void save() {
        User actual = users.save(user);
        assertAll(
                () -> assertThat(actual.id()).isNotNull(),
                () -> assertThat(actual.email()).isEqualTo(user.email())
        );
    }

    @Test
    @DisplayName("유저 아이디 기준 조회")
    void findById() {
        User expected = users.save(user);
        Optional<User> actual = users.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().id()).isEqualTo(expected.id());
        assertThat(actual.get() == expected).isTrue();
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 수정")
    void update() {
        String name = "mwkwon";
        User expected = users.save(user);
        expected.name(name);
        entityManager.flush();
        entityManager.clear();
        Optional<User> actual = users.findById(expected.id());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().name()).isEqualTo(new UserName(name));
    }

    @Test
    @DisplayName("회원 정보 테이블 정상 삭제")
    void delete() {
        User expected = users.save(user);
        users.delete(expected);
        entityManager.flush();
        entityManager.clear();
        Optional<User> actual = users.findById(expected.id());
        assertThat(actual.isPresent()).isFalse();
    }
}
