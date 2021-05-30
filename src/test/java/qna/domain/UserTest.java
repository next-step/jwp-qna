package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
    public static final User SUNJU = new User("sunju", "password", "name", "sunju@slipp.net");

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(SUNJU.getId()).isNull();
        userRepository.findAll();
        User actualUser1 = userRepository.save(SUNJU);
        assertThat(actualUser1.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualUser1.getUserId()).isEqualTo(SUNJU.getUserId());
        assertThat(actualUser1.getPassword()).isEqualTo(SUNJU.getPassword());
        assertThat(actualUser1.getName()).isEqualTo(SUNJU.getName());
        assertThat(actualUser1.getEmail()).isEqualTo(SUNJU.getEmail());

        // 리스트 테스트
        User actualUser2 = userRepository.save(SANJIGI);
        List<User> userList = userRepository.findAll();
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList).containsExactly(actualUser1, actualUser2);
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalse() {
        User expected = userRepository.save(JAVAJIGI);
        Optional<User> actual = userRepository.findByUserId(expected.getUserId());

        assertThat(actual.get()).isEqualTo(expected);
    }
}
