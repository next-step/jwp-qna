package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    public static final User JAVAJIGI = new User(1L,"javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L,"sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @DisplayName("주어진 ID에 해당하는 사용자을 리턴한다.")
    void 주어진_ID에_해당하는_사용자를_리턴한다() {
        // given
        User user = userRepository.findAll().get(0);

        // when
        User result = userRepository.findById(user.getId()).get();

        // then
        assertThat(result).isEqualTo(user);
    }

    @Test
    @DisplayName("사용자 이름을 수정한다.")
    void 사용자_이름을_수정한다() {
        // given
        User user = userRepository.findAll().get(0);
        String newName = "박성민";

        // when
        user.updateName(newName);

        // then
        List<User> result = userRepository.findByName(new Name(newName));
        assertThat(result).containsExactly(user);
    }

    @Test
    @DisplayName("모든 사용자를 삭제한다.")
    void 모든_사용자를_삭제한다() {
        // given
        List<User> prevResult = userRepository.findAll();
        assertThat(prevResult.size()).isGreaterThan(0);

        // when
        userRepository.deleteAll();

        // then
        List<User> result = userRepository.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("주어진 사용자 ID에 해당하는 사용자를 리턴한다.")
    void 주어진_사용자_ID에_해당하는_사용자를_리턴한다() {
        // given
        User user = userRepository.findAll().get(0);
        UserId userId = user.getUserId();

        // when
        User result = userRepository.findByUserId(userId).get();

        // then
        assertThat(result).isEqualTo(user);
    }
}
