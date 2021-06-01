package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswersTest {

    Answers answers;

    @BeforeEach
    void setUp() {
        Question question = new Question("title1", "contents1").writeBy(UserRepositoryTest.JAVAJIGI);
        Answer answer = new Answer(UserRepositoryTest.JAVAJIGI, question, "Answers Contents1");
        Answer answer2 = new Answer(UserRepositoryTest.JAVAJIGI, question, "Answers Contents2");
        Answer answer3 = new Answer(UserRepositoryTest.SANJIGI, question, "Answers Contents3");
        answers = new Answers(Arrays.asList(answer, answer2, answer3));
    }

    @DisplayName("여러 답변의 작성자 확인")
    @Test
    void 여러_답변의_작성자_확인() {
        assertThatThrownBy(() -> answers.validateOwner(UserRepositoryTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
