package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

        DeleteHistory deleteHistory = question.delete(writer, LocalDateTime.now());

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.QUESTION, 1L, writer));
    }

    @Test
    @DisplayName("질문을 삭제하는 경우, ANSWER 타입의 삭제 기록이 남는다.")
    void 답변_삭제_기록() throws CannotDeleteException {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        Answer answer = new Answer(10L, writer, question, "answer contents");

        DeleteHistory deleteHistory = answer.delete(writer, LocalDateTime.now());

        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, 10L, writer));
    }

    @Test
    @DisplayName("질문을 삭제한 시각과 삭제 히스토리가 생성된 시각이 같아야 한다.")
    void 질문_삭제_시각_삭제_히스토리_생성_시각_동일() throws CannotDeleteException {
        User writer = new User(100L, "user", "password", "name", "email");
        Question question = new Question(1L, "title", "contents")
            .writeBy(writer);

        LocalDateTime deleteTime = LocalDateTime.now();
        DeleteHistory deleteHistory = question.delete(writer, deleteTime);

        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, writer, deleteTime);
        assertThat(deleteHistory).isEqualTo(expected);
        assertThat(deleteTime).isEqualTo(expected.getCreateDate());
    }

}
