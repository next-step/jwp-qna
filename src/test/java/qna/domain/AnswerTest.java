package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import qna.UnAuthorizedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("답변")
public class AnswerTest {
    public static final String ANSWERS_CONTENTS_1 = "Answers Contents1";
    public static final String ANSWERS_CONTENTS_2 = "Answers Contents2";
    public static final Answer ANSWER_1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.QUESTION_1, ANSWERS_CONTENTS_1);
    public static final Answer ANSWER_2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.QUESTION_2, ANSWERS_CONTENTS_2);

    @DisplayName("답변 생성")
    @Test
    void constructor() {
        assertThatNoException().isThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, QuestionTest.QUESTION_1, ANSWERS_CONTENTS_1));
    }

    @DisplayName("작성자가 없을 수 없다.")
    @Test
    void writer_not_null() {
        assertThatThrownBy(() -> new Answer(1L, null, QuestionTest.QUESTION_1, ANSWERS_CONTENTS_1))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
