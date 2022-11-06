package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.User;
import qna.domain.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository users;

    @BeforeEach
    void setUp() {
        users.save(new User("diqksrk", "diqksrk", "강민준", "diqksrk123@naver.com"));
    }

    @Test
    @DisplayName("user테이블 저장 테스트")
    void save() {
        User expected = new User("diqksrk123", "diqksrk", "강민준", "diqksrk123@naver.com");
        User actual = users.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("user테이블 select 테스트")
    void findByUserId() {
        Optional<User> expected = users.findByUserId("diqksrk");

        assertThat(expected.get().getUserId()).isEqualTo("diqksrk");
    }

    @Test
    @DisplayName("user테이블 update 테스트")
    void updateName() {
        User expected = users.findByUserId("diqksrk")
                            .get();
        expected.setName("박박박");
        User actual = users.findByUserId("diqksrk")
                            .get();

        assertThat(actual.getName()).isEqualTo("박박박");
    }

    @Test
    @DisplayName("user테이블 delete 테스트")
    void delete() {
        User user = users.findByUserId("diqksrk").get();
        users.delete(user);

        assertThat(users.findByUserId("diqksrk").isPresent()).isFalse();
    }
}