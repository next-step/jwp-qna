package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        //Given
        savedUser = new User("applemango", "pw", "name", "contents");
        savedUser = userRepository.save(savedUser);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        User actual = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(actual).isSameAs(savedUser);
    }

    @DisplayName("save()실행 시, id/createdAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_save_result() {
        //Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isNotNull();
    }

    @DisplayName("update()실행 시, updatedAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_updatedAt() {
        //When
        savedUser.setEmail("pythonjigi@slipp.net");

        //Then
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @DisplayName("동일한 user_id로 생성하려는 경우, exception이 발생한다")
    @Test
    void check_unique_constraints() {
        //Given
        String targetUserId = savedUser.getUserId();
        User newUser = new User(targetUserId, "password", "name", "pythonjigi@slipp.net");

        //When + Then
        assertThatThrownBy(() -> userRepository.save(newUser)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("id를 통해 user 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        User user = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(savedUser.getId());
        assertThat(user.getUserId()).isEqualTo(savedUser.getUserId());
    }

    @DisplayName("user_id를 통해 user 객체를 조회하는지 확인한다")
    @Test
    void check_findByUserId() {
        //Then
        User foundUser = userRepository.findByUserId(savedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isSameAs(savedUser);
    }
}
