package qna.domain.entity;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionTest {
    public static final Question QUESTION_OF_JAVAJIGI = new Question("title1", "contents1")
            .writeBy(UserTest.USER_JAVAJIGI);

    public static final Question QUESTION_OF_SANJIGI = new Question("title2", "contents2")
            .writeBy(UserTest.USER_SANJIGI);

    @Test
    @Order(1)
    @DisplayName("질문 정보 확인")
    void check() {
        assertAll(
            () -> assertThat(QUESTION_OF_JAVAJIGI.getTitle()).isEqualTo("title1"),
            () -> assertThat(QUESTION_OF_JAVAJIGI.getContents()).isEqualTo("contents1"),
            () -> assertThat(QUESTION_OF_JAVAJIGI.getWriter()).isEqualTo(UserTest.USER_JAVAJIGI)
        );
    }

    @Test
    @DisplayName("질문 작성자 확인")
    void isOwner() {
        assertAll(
            () -> assertThat(QUESTION_OF_JAVAJIGI.isOwner(UserTest.USER_JAVAJIGI)).isTrue(),
            () -> assertThat(QUESTION_OF_JAVAJIGI.isOwner(UserTest.USER_SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("질문 정보 확인")
    void addAnswer() {
        QUESTION_OF_JAVAJIGI.addAnswer(AnswerTest.ANSWER_OF_SANJIGI);

        assertAll(
            () -> assertThat(QUESTION_OF_JAVAJIGI.getAnswers().size()).isEqualTo(1),
            () -> assertThat(QUESTION_OF_JAVAJIGI.getAnswers()).contains(AnswerTest.ANSWER_OF_SANJIGI)
        );
    }

    @Test
    @DisplayName("답변 삭제 확인")
    void deleted() {
        QUESTION_OF_JAVAJIGI.deleted();

        assertAll(
            () -> assertThat(QUESTION_OF_JAVAJIGI.isDeleted()).isTrue()
        );
    }

}