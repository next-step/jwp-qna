package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersTest {

    private User writer;
    private Question question;
    private Answer answer, answer2;


    @BeforeEach
    void setUp() {
        writer = UserTest.userSample(1L);
        question = QuestionTest.questionSample(1L, writer);
        answer = AnswerTest.answerSample(1L, writer, question);
        answer2 = AnswerTest.answerSample(2L, writer, question);
    }

    @Test
    @DisplayName("질문 일급컬렉션을 생성한다")
    void create_answers_test() {
        Answers answers = new Answers(Arrays.asList(answer, answer2));

        assertAll(
                () -> assertThat(answers.contains(answer)).isTrue(),
                () -> assertThat(answers.contains(answer2)).isTrue()
        );
    }

    @Test
    @DisplayName("주어진 질문의 포함 여부를 반환한다")
    void contains_answer_test() {
        Answers answers = new Answers(Arrays.asList(answer, answer2));
        boolean isContainsAnswer = answers.contains(answer);

        assertThat(isContainsAnswer).isTrue();
    }

    @Test
    @DisplayName("답변을 모두 삭제하면 그에 해당하는 삭제 이력을 반환한다.")
    void delete_all_answers_then_return_delete_histories_test() throws CannotDeleteException {
        Answers answers = new Answers(Arrays.asList(answer, answer2));
        DeleteHistories expectedDeleteHistories = new DeleteHistories(Arrays.asList(
                DeleteHistory.ofAnswer(answer),
                DeleteHistory.ofAnswer(answer2)
        ));

        DeleteHistories answerDeleteHistories = answers.deleteAll(writer);

        assertThat(answerDeleteHistories).isEqualTo(expectedDeleteHistories);
    }
}
