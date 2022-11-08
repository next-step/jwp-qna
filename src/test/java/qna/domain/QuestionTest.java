package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(Q1.getTitle()).isEqualTo("title1"),
                () -> assertThat(Q2.getTitle()).isEqualTo("title2"),
                () -> assertThat(Q1.getContents()).isEqualTo("contents1"),
                () -> assertThat(Q2.getContents()).isEqualTo("contents2"),
                () -> assertThat(Q1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(Q2.getWriterId()).isEqualTo(UserTest.SANJIGI.getId())
        );
    }
}
