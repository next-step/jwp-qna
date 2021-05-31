package qna.domain.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserPasswordTest {

    @Test
    void create() {
        String expected = "userPassword";
        UserPassword userPassword = new UserPassword(expected);

        assertThat(userPassword.get()).isEqualTo(expected);
    }

    @Test
    void invalid_길이가_짧은비밀번호_Test() {
        String expected = "";

        assertThatThrownBy(() ->
                new UserPassword(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_길이가_긴비밀번호_Test() {
        String expected = "123456789012345678901";

        assertThatThrownBy(() ->
                new UserPassword(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_null_유저비밀번호_Test() {
        String expected = null;

        assertThatThrownBy(() ->
                new UserPassword(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }
    
}