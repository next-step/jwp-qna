package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserTest {
    @Autowired
    private UserRepository userRepository;

    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @DisplayName("저장한_객체와_저장된_객체_비교")
    @Test
    void 저장한_객체와_저장된_객체_비교() {
        User user = new User("gt@gt.com", "gt.kim", "secret", "gtgt");
        User actual = userRepository.save(user);
        assertThat(actual).isEqualTo(user);
    }

    @DisplayName("not null 컬럼에 null을 저장")
    @Test
    void notNull_컬럼에_null을_저장() {
        User user = new User("gt@gt.com", "gt.kim", null, "gtgt");

        assertThatThrownBy(()-> userRepository.save(user)).isInstanceOf(Exception.class);
    }

    @DisplayName("update 테스트(변경감지)")
    @Test
    void update() {
        User user = new User("gt@gt.com", "gt.kim", "secret", "gtgt");
        User actual = userRepository.save(user);

        user.setUserId("gt.kim");
        assertThat(actual.getUserId()).isEqualTo("gt.kim");
    }
}
