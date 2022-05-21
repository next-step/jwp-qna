package qna.domain;

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
    private UserRepository userRepository;

    @Test
    void save() {
        final User YONG = userRepository.save(new User( "yong", "password", "name", "yong@nextstep.com"));
        assertThat(userRepository.findByUserId(YONG.getUserId()).get()).isNotNull();
    }

    @Test
    public void findByName() {
        userRepository.save(UserTest.JAVAJIGI);
        final User YONG = userRepository.findByName(UserTest.JAVAJIGI.getName()).get();
        assertThat(YONG).isNotNull();
    }

    @Test
    @DisplayName("같은 유저 아이디로 중복된 유저가 있을 수 없다.")
    public void duplication_user() {
        userRepository.save(UserTest.JAVAJIGI);
        assertThatThrownBy(() ->
            userRepository.save(new User(UserTest.JAVAJIGI.getUserId(), "password", "name", "yong@nextstep.com"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
