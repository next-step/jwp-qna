package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class AnswersTest {
    private final Answers answers = new Answers(new ArrayList<>(Arrays.asList(AnswerTest.A1, AnswerTest.A2)));

    @Test
    @DisplayName("답변 작성자와 로그인 유저가 일치하지 않으면 삭제할 수 없다.")
    void checkExceptionDeleteAnswersWithInvalidUser() throws CannotDeleteException {
        for (Answer answer : answers.getAnswers()) {
            assertThatThrownBy(() -> answer.delete(UserTest.EUNJI)).isExactlyInstanceOf(CannotDeleteException.class);
        }
    }

    @Test
    @DisplayName("답변을 삭제하면 deleted 필드의 상태값이 true 로 변경된다.")
    void checkDeleteAnswers() throws CannotDeleteException {
        for (Answer answer : answers.getAnswers()) {
            answer.delete(answer.getUser());
        }

        for (Answer answer : answers.getAnswers()) {
            assertThat(answer.isDeleted()).isTrue();
        }
    }

    @Test
    @DisplayName("답변 삭제 히스토리를 생성할 수 있다.")
    void checkCreateDeleteHistories() {
        DeleteHistories histories = answers.createHistories();
        List<DeleteHistory> deleteHistories = histories.toList();
        for (DeleteHistory deleteHistory : deleteHistories) {
            assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
        }
    }
}