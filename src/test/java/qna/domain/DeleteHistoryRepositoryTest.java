package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class DeleteHistoryTest {

    @Test
    @DisplayName("질문을 삭제하는 경우, QUESTION 타입의 삭제 기록이 남는다.")
    void 질문_삭제_기록() throws CannotDeleteException {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        DeleteHistory deleteHistory = question.delete(writer);

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.QUESTION, 1L, writer));
    }

    @Test
    @DisplayName("삭제하지 않은 질문의 삭제 기록을 남기는 경우 예외 발생")
    void 삭제하지_않은_질문을_삭제_기록_남기는_경우() {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        assertThatIllegalStateException().isThrownBy(() -> question.toDeleteHistory())
            .withMessage("삭제시에만 기록을 남길 수 있습니다.");
    }

    @Test
    @DisplayName("질문을 삭제하는 경우, ANSWER 타입의 삭제 기록이 남는다.")
    void 답변_삭제_기록() throws CannotDeleteException {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        Answer answer = new Answer(10L, writer, question, "answer contents");

        DeleteHistory deleteHistory = answer.delete(writer);

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, 10L, writer));
    }

    @Test
    @DisplayName("삭제하지 않은 답변의 삭제 기록을 남기는 경우 예외 발생")
    void 삭제하지_않은_답변을_삭제_기록_남기는_경우() {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        Answer answer = new Answer(10L, writer, question, "answer contents");

        assertThatIllegalStateException().isThrownBy(() -> answer.toDeleteHistory())
            .withMessage("삭제시에만 기록을 남길 수 있습니다.");
    }

}
