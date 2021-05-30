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
        // 데이터 테스트
        assertThat(UserTest.JAVAJIGI.getId()).isNull();
        userRepository.findAll();
        User actualUser1 = userRepository.save(UserTest.JAVAJIGI);
        assertThat(actualUser1.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualUser1.getUserId()).isEqualTo(UserTest.JAVAJIGI.getUserId());
        assertThat(actualUser1.getPassword()).isEqualTo(UserTest.JAVAJIGI.getPassword());
        assertThat(actualUser1.getName()).isEqualTo(UserTest.JAVAJIGI.getName());
        assertThat(actualUser1.getEmail()).isEqualTo(UserTest.JAVAJIGI.getEmail());
        assertThat(actualUser1.getCreatedAt()).isNotNull();
        assertThat(actualUser1.getUpdatedAt()).isNotNull();

        // 리스트 테스트
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
