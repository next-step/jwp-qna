package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.fixtures.AnswerTestFixture;
import qna.fixtures.QuestionTestFixture;
import qna.fixtures.UserTestFixture;
import qna.message.AnswerMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {

    @Test
    @DisplayName("답변을 생성한다")
    void create_answer_test() {
        // given && when
        Answer answer = new Answer(UserTestFixture.손상훈, QuestionTestFixture.질문_작성자는_손상훈, "contents");

        // then
        assertThat(answer).isEqualTo(AnswerTestFixture.답변_작성자는_손상훈);
    }

    @Test
    @DisplayName("답변 생성시 글쓴이가 누락되면 [UnAuthorizedException] 예외처리한다")
    void create_answer_without_writer_test() {
        // given
        Question question = QuestionTestFixture.질문_작성자는_손상훈;

        // when & then
        assertThatThrownBy(() -> new Answer(null, question, "테스트 입니다."))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(AnswerMessage.ERROR_WRITER_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("답변 생성시 질문이 누락되면 [NotFoundException] 예외처리한다")
    void create_answer_without_question_test() {
        // given
        User writer = UserTestFixture.손상훈;

        // when & then
        assertThatThrownBy(() -> new Answer(writer, null, "테스트 입니다."))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(AnswerMessage.ERROR_QUESTION_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("주어진 owner와 답변 owner를 비교하여 동일 여부를 반환한다 ")
    void is_match_owner_with_other_owner() {
        // given
        User owner = UserTestFixture.손상훈;
        Answer answer = AnswerTestFixture.답변_작성자는_손상훈;

        // when
        boolean isEqualOwner = answer.isOwner(owner);

        // then
        assertThat(isEqualOwner).isTrue();
    }

    @Test
    @DisplayName("답변 삭제시 삭제 이력을 반환한다")
    void delete_answer_then_return_history_test() {
        // given
        User writer = UserTestFixture.손상훈;
        Question question = QuestionTestFixture.질문_작성자는_손상훈;
        Answer answer = new Answer(1L, writer, question, "content1");

        // when
        DeleteHistory deleteHistory = answer.delete();

        // then
        assertAll(
                () -> assertThat(answer.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory).isEqualTo(DeleteHistory.ofAnswer(answer))
        );
    }
}
