package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        this.savedUser = userRepository.save(UserTest.JAVAJIGI);
    }

    @DisplayName("저장 테스트")
    @Test
    void save() {
        assertAll(
                () -> assertThat(savedUser.getId()).isNotNull(),
                () -> assertThat(savedUser.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail()),
                () -> assertThat(savedUser.getName()).isEqualTo(UserTest.JAVAJIGI.getName()),
                () -> assertThat(savedUser.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword())
        );
    }

    @DisplayName("동일성 확인")
    @Test
    void findById() {
        // given & when
        User actual = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(savedUser).isEqualTo(actual);
    }

    @DisplayName("flush시 더티체킹을 통해 update쿼리 실행되고 name이 업데이트 된다")
    @Test
    void update() {
        // given
        String newName = "newTestName";

        // when
        savedUser.setName(newName);
        userRepository.flush();
        User actual = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(actual.getName()).isEqualTo(newName);
    }

    @DisplayName("User를 삭제하고 조회하면 empty Optional이 반환된다")
    @Test
    void delete() {
        // given
        userRepository.delete(savedUser);

        // when
        Optional<User> actual = userRepository.findById(savedUser.getId());

        // then
        assertThat(actual).isEqualTo(Optional.empty());
    }
}
