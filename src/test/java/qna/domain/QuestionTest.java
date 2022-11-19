package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.fixtures.UserTestFixture;
import qna.message.QuestionMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question(1L, UserTestFixture.손상훈, "title1", "Contents");
    }

    @Test
    @DisplayName("질문을 생성한다")
    void create_question_test() {
        // given
        User writer = UserTestFixture.손상훈;
        // when
        Question newQuestion = new Question(1L, writer, "title1", "Contents");
        // then
        assertThat(newQuestion).isEqualTo(question);
    }

    @Test
    @DisplayName("질문 생성시 제목이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_title_test() {
        // when & then
        assertThatThrownBy(() -> new Question(null, null, "Contents"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_TITLE_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("질문 생성시 내용이 누락되면 [IllegalArgumentException] 예외처리한다")
    void create_question_without_contents_test() {
        // when & then
        assertThatThrownBy(() -> new Question(null, "title", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(QuestionMessage.ERROR_CONTENTS_SHOULD_BE_NOT_NULL.message());
    }

    @Test
    @DisplayName("주어진 owner와 질문 owner를 비교하여 동일 여부를 반환한다 ")
    void is_match_owner_with_other_owner() {
        // given
        User owner = UserTestFixture.손상훈;

        // when
        boolean isEqualOwner = question.isOwner(owner);

        // then
        assertThat(isEqualOwner).isTrue();
    }

    @Test
    @DisplayName("질문을 삭제하면 soft 삭제 방식으로 진행되고 질문 및 답변들의 삭제 이력을 모두 반환한다")
    void delete_question_test() {
        // given
        User writer = UserTestFixture.손상훈;
        question.addAnswer(new Answer(1L, writer, question, "content1"));
        question.addAnswer(new Answer(2L, writer, question, "content1"));

        // when
        DeleteHistories deleteHistories = question.delete(writer);

        // then
        assertThat(deleteHistories.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("질문 삭제시 로그인 사용자와 질문자가 다르면 [CannotDeleteException] 예외 처리 한다")
    void delete_question_with_other_owner_test() {
        // given
        User otherWriter = new User(2L, "otherId", "password", "다른사람", "email");

        // when & then
        assertThatThrownBy(() -> question.delete(otherWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(QuestionMessage.ERROR_CAN_NOT_DELETE_IF_NOT_OWNER.message());
    }
}
