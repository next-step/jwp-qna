package qna.domain.entity;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerTest {
    public static final Answer ANSWER_OF_JAVAJIGI = new Answer(UserTest.USER_JAVAJIGI, QuestionTest.QUESTION_OF_JAVAJIGI, "Answers Contents1");
    public static final Answer ANSWER_OF_SANJIGI = new Answer(UserTest.USER_SANJIGI, QuestionTest.QUESTION_OF_JAVAJIGI, "Answers Contents2");

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
    @DisplayName("답변 삭제 확인")
    void deleted() {
        ANSWER_OF_SANJIGI.deleted();

        assertAll(
            () -> assertThat(ANSWER_OF_SANJIGI.isDeleted()).isTrue()
        );
    }
}