package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    @Test
    void title_is_not_null_test() {
        assertThatThrownBy(() -> new Question(null, "contents"))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void title_is_not_space_test() {
        assertThatThrownBy(() -> new Question("", "contents"))
                .isInstanceOf(ForbiddenException.class);
    }
}
