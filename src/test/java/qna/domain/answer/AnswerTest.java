package qna.domain.answer;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.question.Question;
import qna.domain.question.QuestionTest;
import qna.domain.user.User;
import qna.domain.user.UserTest;

@DisplayName("답변 테스트")
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 생성")
    void createAnswer() {
        // given
        final User writer = UserGenerator.questionWriter();
        final Question question = QuestionGenerator.question(writer);

        // when
        final Answer answer = new Answer(writer, question, "answer_contents");

        // then
        assertThat(answer).isInstanceOf(Answer.class);
    }

    @Test
    @DisplayName("답변 삭제")
    void deleteAnswer() {
        // given
        final User writer = UserGenerator.questionWriter();
        final Question question = QuestionGenerator.question(writer);
        final Answer answer = new Answer(writer, question, "answer_contents");

        // when
        DeleteHistory delete = answer.delete(writer);

        // then
        assertThat(answer.isDeleted()).isTrue();
        assertThat(delete).isInstanceOf(DeleteHistory.class);
    }

    @Test
    @DisplayName("작성자가 다른 경우 답변 삭제 시 예외 발생")
    void deleteAnswerByNotOwner() {
        // given
        final User writer = UserGenerator.questionWriter();
        final Question question = QuestionGenerator.question(writer);
        final Answer answer = new Answer(writer, question, "answer_contents");

        // when
        assertThatThrownBy(() -> answer.delete(UserGenerator.anotherUser()))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
