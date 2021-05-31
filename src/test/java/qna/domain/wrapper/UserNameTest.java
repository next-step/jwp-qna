package qna.domain.wrapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserNameTest {
    @Test
    void create() {
        String expected = "userName";
        UserName userName = new UserName(expected);

        assertThat(userName.get()).isEqualTo(expected);
    }

    @Test
    void invalid_길이가_짧은_유저네임_Test() {
        String expected = "";

        assertThatThrownBy(() ->
                new UserName(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_길이가_긴_유저네임_Test() {
        String expected = "1234567890" +
                "1234567890" +
                "1";

        assertThatThrownBy(() ->
                new UserName(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_null_유저네임_Test() {
        String expected = null;

        assertThatThrownBy(() ->
                new UserName(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
