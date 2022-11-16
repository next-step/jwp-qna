package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("생성 테스트")
    void questionTest() {
        assertThatNoException().isThrownBy(() -> new Question(1L, "title", "testContent"));
    }
    
    @Test
    @DisplayName("isOwner 메소드 테스트")
    void isOwnerTest() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

}
