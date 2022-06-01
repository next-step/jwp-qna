package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 작성자_미입력_오류_테스트() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "content"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문_미입력_오류_테스트() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "content"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 질문_삭제_답변_삭제_테스트() throws CannotDeleteException {
        // given
        Question question = QuestionTest.Q1;
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        // when
        answer.deleteAnswer(question.getWriter());
        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 작성자_외_다른사용자_답변_오류() {
        // given
        Question question = QuestionTest.Q1;
        Answer answer = new Answer(1L, UserTest.SANJIGI, question, "Answers Contents1");
        // when
        // then
        assertThatThrownBy(() -> answer.deleteAnswer(question.getWriter()))
                .isInstanceOf(CannotDeleteException.class);
    }
}