package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    @Test
    void title_is_not_null_test() {
        assertThatThrownBy(() -> Question.create(null, "contents"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void title_is_not_space_test() {
        assertThatThrownBy(() -> Question.create("", "contents"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
