package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("생성자 테스트")
    void Question_create() {
        assertThat(Q1).isEqualTo(new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("작성자가 본인인지 확인")
    void Question_isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("Answer 추가 확인")
    void Question_addAnswer() {
        Q1.addAnswer(AnswerTest.A2);
        assertThat(AnswerTest.A2.getQuestion()).isEqualTo(Q1);
    }

}
