package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersTest {
    private Answer answer;
    private Answer answer2;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user1", "password", "name", "user1@com");
        answer = new Answer(user, new Question(), "Answers Contents1");
        answer2 = new Answer(user, new Question(), "Answers Contents2");
    }

    @Test
    void 모든_답변을_삭제한다() {
        // given
        Answers answers = new Answers();
        answers.add(answer);
        answers.add(answer2);

        DeleteHistories deleteHistories = new DeleteHistories();
        // when
        answers.deleteAll(deleteHistories, user);
        // then
        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(answer2.isDeleted()).isTrue()
        );
    }

    @Test
    void 다른사람이_작성한_답변을_삭제할_경우_예외가_발생한다() {
        // given
        User loginUser = new User(2L, "user1", "password", "name", "user1@com");
        Answers answers = new Answers();
        answers.add(answer);

        DeleteHistories deleteHistories = new DeleteHistories();
        // when & then
        assertThatThrownBy(() ->
                answers.deleteAll(deleteHistories, loginUser)
        ).isInstanceOf(CannotDeleteException.class);
    }

}