package qna.domain.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserIdTest {

    @Test
    void create() {
        String expected = "userId";
        UserId userId = new UserId(expected);

        assertThat(userId.get()).isEqualTo(expected);
    }

    @Test
    void invalid_길이가_짧은이름_Test() {
        String expected = "";

        assertThatThrownBy(() ->
                new UserId(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_길이가_긴이름_Test() {
        String expected = "123456789012345678901";

        assertThatThrownBy(() ->
                new UserId(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_null_유저아이디_Test() {
        String expected = null;

        assertThatThrownBy(() ->
                new UserId(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
