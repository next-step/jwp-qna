package qna.domain.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserEmailTest {

    @Test
    void create() {
        String expected = "userEmail";
        UserEmail userEmail = new UserEmail(expected);

        assertThat(userEmail.get()).isEqualTo(expected);
    }

    @Test
    void invalid_길이가_긴_유저네임_Test() {
        String expected = "1234567890" +
                "1234567890" +
                "1234567890" +
                "1234567890" +
                "12345678901";

        assertThatThrownBy(() ->
                new UserEmail(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
