package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {
    @Test
    @DisplayName("삭제시 다른 사람의 답변이 있을 경우 오류")
    void delete_error_if_not_equals() {
        Answers answers = new Answers(Arrays.asList(
            new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"),
            new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2"),
            new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3")
        ));

        assertThatThrownBy(() -> answers.delete(UserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }
}
