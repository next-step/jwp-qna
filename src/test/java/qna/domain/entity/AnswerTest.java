package qna.domain.entity;

import org.junit.jupiter.api.*;
import qna.CannotDeleteException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerTest {
    public static final Answer ANSWER_OF_JAVAJIGI = new Answer(1L, UserTest.USER_JAVAJIGI, "Answers Contents1")
            .toQuestion(QuestionTest.QUESTION_OF_JAVAJIGI);

    public static final Answer ANSWER_OF_SANJIGI = new Answer(2L, UserTest.USER_SANJIGI, "Answers Contents2")
            .toQuestion(QuestionTest.QUESTION_OF_SANJIGI);

    @Test
    @Order(1)
    @DisplayName("답변 정보 확인")
    void check() {
        assertAll(
            () -> assertThat(ANSWER_OF_JAVAJIGI.getContents()).isEqualTo("Answers Contents1"),
            () -> assertThat(ANSWER_OF_JAVAJIGI.getWriter()).isEqualTo(UserTest.USER_JAVAJIGI),
            () -> assertThat(ANSWER_OF_JAVAJIGI.getQuestion()).isEqualTo(QuestionTest.QUESTION_OF_JAVAJIGI)
        );
    }

    @Test
    @DisplayName("답변 작성자 확인")
    void isOwner() {
        assertAll(
            () -> assertThat(ANSWER_OF_JAVAJIGI.isOwner(UserTest.USER_JAVAJIGI)).isTrue(),
            () -> assertThat(ANSWER_OF_JAVAJIGI.isOwner(UserTest.USER_SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("해당 답변에 주인이 삭제하는 경우")
    void deleted() throws CannotDeleteException {
        ANSWER_OF_SANJIGI.deleted(UserTest.USER_SANJIGI);

        assertAll(
            () -> assertThat(ANSWER_OF_SANJIGI.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("다른 작성자가 작성한 답변을 삭제하는 경우 예외가 발생한다.")
    void otherDeleted() {
        assertThatThrownBy(() -> ANSWER_OF_SANJIGI.deleted(UserTest.USER_JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}