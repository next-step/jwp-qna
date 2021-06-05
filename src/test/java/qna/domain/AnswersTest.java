package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswersTest {

    private final User user = new User("id", "password", "name", "email");
    private final Question question = new Question("title", "contents", user);
    private final Answer answer = new Answer(user, question, "contents");
    private final Answer answer2 = new Answer(user, question, "contents2");

    @DisplayName("Answer List 를 Answers 생성자 argument 로 전달했을 때 Answers 객체가 생성되는지 테스트")
    @Test
    void given_ListOfAnswer_when_ConstructAnswers_then_AnswersIsNotNull() {
        // given
        final List<Answer> answerList = Arrays.asList(answer, answer2);

        // when
        final Answers answers = new Answers(answerList);

        // then
        assertThat(answers).isNotNull();
    }

    @DisplayName("null 을 Answers 생성자의 argument 로 전달했을 때 예외가 발생하는지 테스트")
    @ParameterizedTest
    @NullSource
    void given_Null_when_ConstructAnswers_then_ThrownIllegalArgumentException(final List<Answer> answerList) {
        // when
        final Throwable throwable = catchThrowable(() -> new Answers(answerList));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Answer 를 Answers 에 추가하도록 요청하면 Answers 의 size 가 1 이 되는지 테스트")
    @Test
    void given_Answer_when_Request_Add_A_Answer_then_Answers_has_a_Answer() {
        // given
        final Answers answers = new Answers();

        // when
        answers.add(answer);

        // then
        assertThat(answers.size()).isEqualTo(1);
    }

    @DisplayName("Answer 2개가 있는 리스트를 Answers 에게 전달한 뒤, 작성자로 삭제 요청을 하면 DeleteHistory 가 반환되는 테스트")
    @Test
    void given_2Answers_when_RequestDeleteToAnswers_then_2DeleteHistoryIsReturned() throws CannotDeleteException {
        // given
        final List<Answer> answerList = Arrays.asList(answer, answer2);
        final Answers answers = new Answers(answerList);

        // when
        final List<DeleteHistory> deleteHistories = answers.delete(user);

        // then
        assertAll(
                () -> assertThat(deleteHistories.size()).isEqualTo(2)
        );
    }
}
