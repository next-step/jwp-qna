package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest extends NewEntityTestBase {

    @Test
    @DisplayName("동일성 테스트")
    void test1() {
        Optional<User> id2 = userRepository.findByUserId(NEWUSER1.getUserId());

        assertThat(id2.get()).isSameAs(NEWUSER1);
    }

    @Test
    @DisplayName("조회 테스트")
    void test2() {
        List<User> all = userRepository.findAll();

        assertThat(all).containsExactly(NEWUSER1, NEWUSER2);
    }
}