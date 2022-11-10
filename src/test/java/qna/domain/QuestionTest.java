package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("답변")
public class QuestionTest {
    public static final Question QUESTION_1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question QUESTION_2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("질문 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }
}
