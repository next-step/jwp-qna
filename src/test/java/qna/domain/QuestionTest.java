package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void 동등성() {
        assertThat(new Question(1L, "title", "contents"))
            .isEqualTo(new Question(1L, "title", "contents"));

        assertThat(new Question(1L, "title", "contents"))
            .isNotEqualTo(new Question(2L, "title", "contents"));
    }

    @Test
    void 작성자_등록() {
        Q1.writeBy(UserTest.JAVAJIGI);
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse();
    }
}
