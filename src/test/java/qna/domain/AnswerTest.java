package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    private Question question;
    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "1234", "javajigi", "a@email.com");
        sanjigi = new User("sanjigi", "1234", "sanjigi", "b@email.com");
        question = new Question("title1", "contents1").writeBy(javajigi);
    }

    @Test
    void 답변_작성자_확인() {
        Answer answer = new Answer(sanjigi, question, "Answers Contents1");

        assertThatThrownBy(() -> answer.validateSameOwnerInAnswer(javajigi));
    }
}
