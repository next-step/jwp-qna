package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private User savedJavajigi, savedSanjigi;
    
    @BeforeEach
    void saveUser() {
        savedJavajigi = userRepository.save(UserTest.JAVAJIGI);
        savedSanjigi = userRepository.save(UserTest.SANJIGI);
    }
    
    @Test
    @DisplayName("저장")
    void saveTest() {
        assertAll(
            () -> assertThat(userRepository.count()).isEqualTo(2),
            () -> assertThat(UserTest.JAVAJIGI.getPassword()).isEqualTo(savedJavajigi.getPassword()),
            () -> assertThat(UserTest.SANJIGI.getName()).isEqualTo(savedSanjigi.getName())
        );
    }
    
    @Test
    @DisplayName("사용자 ID에 null이 들어가지 못한다")
    void insertNullUserIdTest() {
        User testUser = new User(3L, null, "password", "name", "yeji.seo@slipp.net");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(testUser);
        });
    }

    @Test
    @DisplayName("사용자 ID는 중복될 수 없다")
    void insertNotUniqueIdTest() {
        User testUser = new User(3L, "javajigi", "password", "name", "yeji.seo@slipp.net");
        assertThrows(ConstraintViolationException.class, () ->{
            userRepository.save(testUser);
        });
    }
    
    @Test
    @DisplayName("수정")
    void updateTest() {
        savedJavajigi.setName("name_");
        User selectedUser = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        assertThat(selectedUser.getName()).isEqualTo(savedJavajigi.getName());
    }
    
    @Test
    @DisplayName("삭제")
    void deleteTest() {
        userRepository.delete(savedJavajigi);
        assertAll(
            () -> assertThat(userRepository.count()).isEqualTo(1),
            () -> assertThat(userRepository.existsById(savedJavajigi.getId())).isFalse()
        );
    }
    
    @Test
    @DisplayName("사용자 ID로 찾기")
    void findByUserIdTest() {
        assertThat(userRepository.findByUserId("javajigi").get()).isEqualTo(savedJavajigi);
    }
    
}
