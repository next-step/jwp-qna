package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class QuestionEntityTest {

    private final Long question_id = 1L;
    private final Long user_id = 1000L;

    private final Long other_user_id = 1111L;
    private final User writer = new User(user_id, "", "", "", "");

    private final User otherUser = new User(other_user_id, "", "", "", "");
    private final Question question = new Question(question_id, "", "").writeBy(writer);

    @Test
    @DisplayName("엔티티의 delete가 실행되면, deleted가 true가 되고, 삭제 히스토리를 반환해야 한다")
    void delete_should_deleted_true_and_return_remove_history() throws CannotDeleteException {
        // when
        DeleteHistories actual = question.delete(writer);

        // then
        DeleteHistory expected = DeleteHistory.ofQuestion(question_id, writer);
        assertThat(question.isDeleted()).isTrue();
        assertThat(actual.getAll()).usingFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createDate")
                .containsExactly(expected);
    }

    @Test
    @DisplayName("질문글 작성유저와, 로그인유저가 서로 다를 경우 에러가 발생한다")
    void delete_should_throw_exception_if_writer_and_requestor_diff() {
        // when
        assertThatThrownBy(() -> question.delete(otherUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문글에 답변이 있고, 로그인 유저가 질문글, 답변글을 쓰지 않았다면, 삭제할 수 없다는에러가 발생한다")
    void delete_should_throw_exception_if_answer_writer_and_requestor_diff() {
        // given
        question.addAnswer(new Answer(otherUser, question, ""));

        // when
        assertThatThrownBy(() -> question.delete(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
