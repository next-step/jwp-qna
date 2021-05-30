package qna.domain.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTitleTest {

    @Test
    void create() {
        String expected = "title";
        QuestionTitle title = new QuestionTitle(expected);

        assertThat(title.get()).isEqualTo(expected);
    }

    @Test
    void invalid_길이가_짧은_타이틀_Test() {
        String expected = "";

        assertThatThrownBy(() ->
                new QuestionTitle(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_길이가_긴_타이틀_Test() {
        String expected = "12345678901234567890" +
                "12345678901234567890" +
                "12345678901234567890" +
                "12345678901234567890" +
                "12345678901234567890" +
                "1";

        assertThatThrownBy(() ->
                new QuestionTitle(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalid_null_타이틀_Test() {
        String expected = null;

        assertThatThrownBy(() ->
                new QuestionTitle(expected)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
