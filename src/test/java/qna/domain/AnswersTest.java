package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.fixtures.QuestionTestFixture;
import qna.fixtures.UserTestFixture;
import qna.message.AnswerMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    private Answers answers;

    @BeforeEach
    void setUp() {
        User writer = UserTestFixture.손상훈;
        Question question = QuestionTestFixture.질문_작성자는_손상훈;

        Answer answer1 = new Answer(1L, writer, question, "content1");
        Answer answer2 = new Answer(2L, writer, question, "content2");
        Answer answer3 = new Answer(3L, writer, question, "content3");
        answers = new Answers(answer1, answer2, answer3);
    }

    @Test
    @DisplayName("Answers 삭제시 삭제 이력을 모두 반환한다")
    void delete_all_answers_then_result_delete_histories_test() {
        // given
        User writer = UserTestFixture.손상훈;

        // when
        DeleteHistories deleteHistories = answers.deleteAll(writer);

        // then
        assertThat(deleteHistories.getAll()).hasSize(3);
    }

    @Test
    @DisplayName("Answers 삭제시 질문자와 답변자가 다르면 [CannotDeleteException] 예외처리 한다")
    void delete_all_answers_if_not_owner_throw_CannotDeleteException_test() {
        // given
        User otherWriter = new User(2L, "otherId", "password", "name", "email");

        // when & then
        assertThatThrownBy(() -> answers.deleteAll(otherWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(AnswerMessage.ERROR_CAN_NOT_DELETE_IF_OWNER_NOT_EQUALS.message());
    }
}
