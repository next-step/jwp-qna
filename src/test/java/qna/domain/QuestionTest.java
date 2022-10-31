package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    
    @Test
    void Question_title_test() {
        assertThat(Q1.getTitle()).isEqualTo("title1");
    }
    @Test
    void Question_contents_test() {
        assertThat(Q1.getContents()).isEqualTo("contents1");
    }
    @Test
    void Question_owner_test() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isEqualTo(true);
    }
}
