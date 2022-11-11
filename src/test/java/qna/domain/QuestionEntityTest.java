package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class QuestionEntityTest {

    @Test
    @DisplayName("엔티티의 delete가 실행되면, deleted가 true가 되고, 삭제 히스토리를 반환해야 한다")
    void delete_should_deleted_true_and_return_remove_history() throws CannotDeleteException {
        // given
        Long question_id = 1L;
        Long user_id = 1000L;
        String dummy = "baeksoo";
        User writer = new User(user_id, dummy, dummy, dummy, dummy);
        Question question = new Question(question_id, dummy, dummy).writeBy(writer);

        // when
        List<DeleteHistory> actual = question.delete(writer);

        // then
        DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question_id, writer, LocalDateTime.now());
        assertThat(question.isDeleted()).isTrue();
        assertThat(actual).usingFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createDate")
                .containsExactly(expected);
    }

    @Test
    @DisplayName("질문글 작성유저와, 로그인유저가 서로 다를 경우 에러가 발생한다")
    void delete_should_throw_exception_if_writer_and_requestor_diff() {
        // given
        Long question_id = 1L;
        Long user_id = 1000L;
        Long other_user_id = 1111L;
        String dummy = "baeksoo";
        User writer = new User(user_id, dummy, dummy, dummy, dummy);
        User notWriterUser = new User(other_user_id, dummy, dummy, dummy, dummy);
        Question question = new Question(question_id, dummy, dummy).writeBy(writer);

        // when
        assertThatThrownBy(() -> question.delete(notWriterUser))
                .isInstanceOf(CannotDeleteException.class);
    }

}
