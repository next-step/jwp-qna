package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(UserTest.SUNJU.getId()).isNull();
        userRepository.findAll();
        User actualUser1 = userRepository.save(UserTest.SUNJU);
        assertThat(actualUser1.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualUser1.getUserId()).isEqualTo(UserTest.SUNJU.getUserId());
        assertThat(actualUser1.getPassword()).isEqualTo(UserTest.SUNJU.getPassword());
        assertThat(actualUser1.getName()).isEqualTo(UserTest.SUNJU.getName());
        assertThat(actualUser1.getEmail()).isEqualTo(UserTest.SUNJU.getEmail());
        assertThat(actualUser1.getCreatedAt()).isNotNull();
        assertThat(actualUser1.getUpdatedAt()).isNotNull();

        // 리스트 테스트
        User actualUser2 = userRepository.save(UserTest.SANJIGI);
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList).containsExactly(actualUser1, actualUser2);
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalse() {
        User expected = userRepository.save(UserTest.JAVAJIGI);
        Optional<User> actual = userRepository.findByUserId(expected.getUserId());

        assertThat(actual.get()).isEqualTo(expected);
    }
}
