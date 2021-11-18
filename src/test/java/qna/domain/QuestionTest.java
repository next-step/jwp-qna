package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 값 확인")
    @Test
    void init() {
        assertAll(
            () -> assertThat(Q1.getTitle()).isEqualTo("title1"),
            () -> assertThat(Q1.getContents()).isEqualTo("contents1"),
            () -> assertThat(Q1.getWriter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }
}
