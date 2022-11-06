package qna.domain;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_저장() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(user.getId()).isNotNull(),
                () -> assertThat(user.equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue(),
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
                () -> assertThat(user.equalsNameAndEmail(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 유저_삭제_시_데이터가_삭제() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        userRepository.deleteById(user.getId());
        Optional<User> actual = userRepository.findById(user.getId());
        assertThat(actual).isNotPresent();
    }

    @Test
    void 유저_아이디_를_통한_유저_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(user.getUserId()).isEqualTo(actual.getUserId());
    }

    @Test
    void 유저_수정() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        User expected = new User("newUserId", "password", "giraffelim", "email");
        user.update(user, expected);
        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(actual.equalsNameAndEmail(expected)).isTrue();
    }
}
