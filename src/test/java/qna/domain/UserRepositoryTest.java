package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("User 생성")
    @Test
    void teat_save() {
        //given & when
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> findUser = userRepository.findById(savedUser.getId());
        //then
        assertAll(
                () -> assertThat(findUser.isPresent()).isTrue(),
                () -> assertThat(savedUser.equals(findUser.get())).isTrue()
        );
    }

    @DisplayName("User Id로 User 조회")
    @Test
    void teat_findByUserId() {
        //given
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        //when
        Optional<User> findUser = userRepository.findByUserId(savedUser.getUserId());
        //then
        assertAll(
                () -> assertThat(findUser.isPresent()).isTrue(),
                () -> assertThat(savedUser.equalsNameAndEmail(findUser.get())).isTrue()
        );
    }

}