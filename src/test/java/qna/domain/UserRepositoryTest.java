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
    void 유저_이름_수정() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        user.setName("giraffelim");
        User actual = userRepository.findByUserId(user.getUserId()).get();
        assertThat(actual.getName()).isEqualTo("giraffelim");
    }
}
