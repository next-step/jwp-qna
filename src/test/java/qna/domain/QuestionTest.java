package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("질문 관련 기능 테스트")
public class QuestionTest {

    User writer1;
    User writer2;

    @BeforeEach
    void setup() {
        writer1 = new User("yulmucha", "pwd", "yul", "yul@google.com");
        writer2 = new User("yujacha", "pwd", "yu", "yu@google.com");
    }

    @Test
    @DisplayName("질문에 답변이 없을 때, 로그인한 사용자 본인이 작성한 질문이라면 삭제 성공")
    void delete_same_writer_no_answer() throws CannotDeleteException {
        Question question = new Question(writer1, "Question A", "Contents A");
        assertThat(question.isDeleted()).isFalse();

        DeleteHistories deleteHistories = question.deletedBy(question.getWriter());

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.toList()).containsExactly(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    @Test
    @DisplayName("질문에 답변이 없을 때, 로그인한 사용자 본인이 작성한 질문이 아니라면 질문 삭제 실패")
    void delete_diff_writer_no_answer() {
        Question question = new Question(writer1, "Question A", "Contents A");

        assertThat(question.isDeleted()).isFalse();
        assertThatThrownBy(() -> question.deletedBy(writer2))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 로그인한 사용자가 작성한 질문이고 본인이 작성한 답변만 존재한다면, 질문 및 답변 삭제 성공")
    void delete_with_answer_same_writer() throws CannotDeleteException {
        Question question = new Question(writer1, "Question A", "Contents A");
        Answer answer = new Answer(writer1, question, "Answer Contents");
        assertThat(question.isDeleted()).isFalse();

        DeleteHistories deleteHistories = question.deletedBy(writer1);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistories.toList()).contains(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()),
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
    }

    @Test
    @DisplayName("질문에 답변이 있을 때, 로그인한 사용자 본인이 작성한 질문이라도 타인이 작성한 답변이 존재한다면 삭제 실패")
    void delete_with_answer_diff_writer() throws CannotDeleteException {
        Question question = new Question(writer1, "Question A", "Contents A");
        Answer answer = new Answer(writer2, question, "Answer Contents");

        assertThat(question.isDeleted()).isFalse();
        assertThat(answer.isDeleted()).isFalse();
        assertThatThrownBy(() -> question.deletedBy(writer1))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 답변을 중복으로 추가할 때 실패")
    void add_duplicate_answer() {
        Question question = new Question(writer1, "Question A", "Contents A");
        Answer answer = new Answer(writer2, question, "Answer Contents");

        assertThatThrownBy(() -> question.addAnswer(answer))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
