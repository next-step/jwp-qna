package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    private Answers answers;

    private User javajigi;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "1234", "javajigi", "a@email.com");
        User sanjigi = new User("sanjigi", "1234", "javajigi", "a@email.com");

        Question question = new Question("title1", "contents1").writeBy(javajigi);

        Answer answer = new Answer(javajigi, question, "Answers Contents1");
        Answer answer2 = new Answer(javajigi, question, "Answers Contents2");
        Answer answer3 = new Answer(sanjigi, question, "Answers Contents3");
        answers = new Answers(Arrays.asList(answer, answer2, answer3));
    }

    @DisplayName("여러 답변의 작성자 확인")
    @Test
    void 여러_답변의_작성자_확인() {
        assertThatThrownBy(() -> answers.validateOwner(javajigi))
                .isInstanceOf(CannotDeleteException.class);
    }
}
