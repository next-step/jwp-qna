package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question의 작성자를 변경할 수 있다.")
    @Test
    void write_by() {
        Question actual = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        actual.writeBy(UserTest.SANJIGI);

        assertThat(actual.isOwner(UserTest.SANJIGI)).isTrue();
    }
}
