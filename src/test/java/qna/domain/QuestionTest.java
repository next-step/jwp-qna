package qna.domain;

import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

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
