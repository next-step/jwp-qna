package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    private final User user = new User(1000L, "", "", "", "");

    private final User otherUser = new User(1111L, "", "", "", "");

    @Test
    @DisplayName("답변 유저와 로그인 유저가 서로 다를 경우 삭제할 수 없다는 예외가 발생해야 한다")
    void deleteAll_throw_exception_if_answer_wrtier_and_login_user_diff() {
        // given
        Answers answers = new Answers();
        answers.add(new Answer(user, new Question(), ""));

        // when
        assertThatThrownBy(() -> answers.deleteAll(otherUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("답변 유저와 로그인 유저가 서로 같을 경우 삭제할 수 있고, deleted가 true가 되고, 삭제 히스토리를 반환해야 한다")
    void deleteAll_get_delete_histories_if_answer_wrtier_and_login_user_same() throws CannotDeleteException {
        // given
        Answers answers = new Answers();
        answers.add(new Answer(user, new Question(), ""));

        // when
        DeleteHistories actual = answers.deleteAll(user);

        // then
        DeleteHistory expected = DeleteHistory.ofAnswer(null, user);
        assertThat(actual.getAll()).usingFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createDate")
                .containsExactly(expected);
    }
}
