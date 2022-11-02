package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "id", "password", "name", "email");
        user2 = new User(null, "id2", "password2", "name2", "email2");
        repository.saveAll(Arrays.asList(user1,user2));
    }

    @Test
    @DisplayName("동일성 테스트")
    void test1() {
        Optional<User> id2 = repository.findByUserId("id2");

        assertThat(id2.get()).isSameAs(user2);
    }

    @Test
    @DisplayName("조회 테스트")
    void test2() {
        List<User> all = repository.findAll();

        assertThat(all).containsExactly(user1,user2);
    }
}