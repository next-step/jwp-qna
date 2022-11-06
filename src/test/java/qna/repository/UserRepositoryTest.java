package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.User;
import qna.fixture.TestUserFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자를 저장할 수 있다")
    @Test
    void save() {
        User expect = TestUserFactory.create("서정국");
        User result = userRepository.save(expect);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getUserId()).isEqualTo(expect.getUserId()),
                () -> assertThat(result.getName()).isEqualTo(expect.getName()),
                () -> assertThat(result.getPassword()).isEqualTo(expect.getPassword()),
                () -> assertThat(result.getEmail()).isEqualTo(expect.getEmail())
        );
    }

    @DisplayName("사용자 계정으로 조회할 수 있다")
    @Test
    void findByUserId() {
        User expect = userRepository.save(TestUserFactory.create("서정국"));

        User result = userRepository.findByUserId(expect.getUserId()).get();

        assertEquals(expect, result);
    }
}
