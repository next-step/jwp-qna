package qna.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.ContentType.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

    @Test
    @DisplayName("답변을 삭제하면 답변의 상태가 삭제상태가 되고, 삭제 내역이 반환된다.")
    void deleteStatusTest() throws Exception {
        LocalDateTime deletedAt = now();
        Answer answer = new Answer(JAVAJIGI, Q1, "answer contents");
        DeleteHistory expectedHistory = DeleteHistory.of(answer, deletedAt);

        DeleteHistory actualHistory = answer.delete(JAVAJIGI, deletedAt);

        assertAll(
            () -> assertThat(answer.isDeleted()).isTrue(),
            () -> assertThat(actualHistory).isEqualTo(expectedHistory)
        );
    }

    @Test
    @DisplayName("자신이 쓴 답변만 삭제할 수 있다.")
    void deleteAuthorizationTest() {
        LocalDateTime deletedAt = now();
        Answer answer = new Answer(JAVAJIGI, Q1, "answer contents");

        assertThatThrownBy(() -> answer.delete(SANJIGI, deletedAt))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변을 질문에 등록했을때 질문에도 답변이 연결되어야 한다.")
    void toQuestionTest() {
        Question question = new Question("title", "contents", JAVAJIGI);
        Answer answer = new Answer(JAVAJIGI, question, "answer contents");

        assertThat(question.getAnswers()).isEqualTo(Answers.of(answer));
    }
}
