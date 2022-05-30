package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("답변 관련 기능 테스트")
public class AnswerTest {

    User writer1;
    User writer2;
    Question question;

    @BeforeEach
    void setup() {
        writer1 = new User("yulmucha", "pwd", "yul", "yul@google.com");
        writer2 = new User("yujacha", "pwd", "yu", "yu@google.com");
        question = new Question(writer1, "Question Title", "Question Contents");
    }

    @Test
    @DisplayName("작성자 본인이라면 삭제 성공")
    void delete_same_writer() throws CannotDeleteException {
        Answer answer = new Answer(writer1, question, "this is answer");
        assertThat(answer.isDeleted()).isFalse();

        DeleteHistory history = answer.deletedBy(answer.getWriter());

        assertThat(answer.isDeleted()).isTrue();
        assertThat(history).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
    }

    @Test
    @DisplayName("작성자 본인이 아니라면 삭제 실패")
    void delete_diff_writer() {
        Answer answer = new Answer(writer1, question, "this is answer");
        assertThat(answer.isDeleted()).isFalse();

        assertThatThrownBy(() -> answer.deletedBy(writer2))
                .isInstanceOf(CannotDeleteException.class);
    }
}
