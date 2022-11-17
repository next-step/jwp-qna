package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;
import qna.fixtures.UserTestFixture;
import qna.message.AnswerMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    @Test
    @DisplayName("Answers 삭제시 삭제 이력을 모두 반환한다")
    void delete_all_answers_then_result_delete_histories_test() {
        User writer = UserTestFixture.createUser();
        Question question = QuestionTestFixture.createQuestionWithWriter(writer);
        Answer answer1 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(1L, writer, question);
        Answer answer2 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(2L, writer, question);
        Answer answer3 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(3L, writer, question);
        Answer answer4 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(4L, writer, question);

        Answers answers = new Answers(answer1, answer2, answer3, answer4);
        DeleteHistories deleteHistories = answers.deleteAll(writer);

        assertThat(deleteHistories.getAll()).contains(
            DeleteHistory.ofAnswer(answer1),
            DeleteHistory.ofAnswer(answer2),
            DeleteHistory.ofAnswer(answer3),
            DeleteHistory.ofAnswer(answer4)
        );
    }

    @Test
    @DisplayName("Answers 삭제시 질문자와 답변자가 다르면 [CannotDeleteException] 예외처리 한다")
    void delete_all_answers_if_not_owner_throw_CannotDeleteException_test() {
        User writer = UserTestFixture.createUserWithId(1L);
        User otherWriter = UserTestFixture.createUserWithId(2L);
        Question question = QuestionTestFixture.createQuestionWithWriter(writer);
        Answer answer1 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(1L, writer, question);
        Answer answer2 = AnswerTestFixture.createAnswerWithIdAndWriterAndQuestion(2L, otherWriter, question);

        Answers answers = new Answers(answer1, answer2);
        assertThatThrownBy(() -> answers.deleteAll(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(AnswerMessage.ERROR_CAN_NOT_DELETE_IF_OWNER_NOT_EQUALS.message());
    }
}
