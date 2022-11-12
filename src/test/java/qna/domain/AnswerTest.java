package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.message.AnswerMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변을 생성한다")
    void create_answer_test() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "테스트 입니다.");
        assertThat(answer).isEqualTo(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "테스트 입니다."));
    }

    @Test
    @DisplayName("답변 생성시 글쓴이가 누락되면 [UnAuthorizedException] 예외처리한다")
    void create_answer_without_writer_test() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "테스트 입니다."))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(AnswerMessage.ERROR_WRITER_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("답변 생성시 질문이 누락되면 [NotFoundException] 예외처리한다")
    void create_answer_without_question_test() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "테스트 입니다."))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(AnswerMessage.ERROR_QUESTION_SHOULD_BE_NOT_NULL.message());
    }
}
