package qna.domain;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User expected;

    @BeforeEach
    void setUp() {
        //Given
        expected = userRepository.save(UserTest.JAVAJIGI);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        User actual = userRepository.findById(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(actual).isSameAs(expected);
    }

    @DisplayName("save()실행 시, id/createdAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_save_result() {
        //Then
        assertThat(expected.getId()).isNotNull();
        assertThat(expected.getCreatedAt()).isNotNull();
    }

    @DisplayName("update()실행 시, updatedAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_updatedAt() {
        //When
        expected.setEmail("pythonjigi@slipp.net");

        //Then
        assertThat(expected.getUpdatedAt()).isNotNull();
    }

    @DisplayName("동일한 user_id로 생성하려는 경우, exception이 발생한다")
    @Test
    void check_unique_constraints() {
        //Given
        User newUser = new User("javajigi", "password", "name", "pythonjigi@slipp.net");

        //When + Then
        assertThatThrownBy(() -> userRepository.save(newUser)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("id를 통해 user 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        User user = userRepository.findById(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(expected.getId());
        assertThat(user.getUserId()).isEqualTo(expected.getUserId());
    }
}
