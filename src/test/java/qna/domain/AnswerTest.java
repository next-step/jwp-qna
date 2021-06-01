package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    private Question question;

    @BeforeEach
    public void setUp() {
        question = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
    }

    @Test
    void 답변_작성자_확인() {
        Answer answer = new Answer(UserRepositoryTest.SANJIGI, question, "Answers Contents1");
        assertThatThrownBy(() -> answer.validateSameOwnerInAnswer(UserRepositoryTest.JAVAJIGI));
    }
}
