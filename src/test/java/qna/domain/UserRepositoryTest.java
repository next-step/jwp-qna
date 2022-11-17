package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void 유저_저장() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(user.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(user.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(user.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
                () -> assertThat(user.getCreatedAt()).isNotNull(),
                () -> assertThat(user.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 유저_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        User actual = userRepository.findById(user.getId()).get();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(actual.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(actual.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(actual.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 유저_삭제() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        userRepository.deleteById(user.getId());
        Optional<User> actual = userRepository.findById(user.getId());
        assertThat(actual).isNotPresent();
    }

    @Test
    void 유저_아이디로_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(user.getUserId()).isEqualTo(actual.getUserId());
    }

    @Test
    void 유저_영속성_초기화후_같은객채_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        flushAndClear();
        User actual = userRepository.findById(user.getId()).get();
        assertThat(actual).isEqualTo(user);
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
