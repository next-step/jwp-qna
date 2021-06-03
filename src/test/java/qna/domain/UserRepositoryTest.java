package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        assertThat(UserTest.JAVAJIGI.getId()).isNull();
        User actualUser = userRepository.save(UserTest.JAVAJIGI);
        assertThat(actualUser.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualUser.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
        assertThat(actualUser.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword());
        assertThat(actualUser.getName()).isEqualTo(UserTest.JAVAJIGI.getName());
        assertThat(actualUser.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail());
        assertThat(actualUser.getCreatedAt()).isNotNull();
        assertThat(actualUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("User 여러명 save 테스트")
    void saveMultipleUserTest() {
        User actualUser1 = userRepository.save(UserTest.JAVAJIGI);
        User actualUser2 = userRepository.save(UserTest.SANJIGI);
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList).containsExactly(actualUser1, actualUser2);
    }

    @Test
    @DisplayName("user id 기준 검색 테스트")
    void findByUserIdTest() {
        User expected = userRepository.save(UserTest.JAVAJIGI);
        User actual = userRepository.findByUserId(expected.getUserId())
                                              .orElseThrow(IllegalArgumentException::new);

        assertThat(actual).isEqualTo(expected);
    }
}
