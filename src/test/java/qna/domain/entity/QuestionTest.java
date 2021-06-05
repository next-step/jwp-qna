package qna.domain.entity;

import org.junit.jupiter.api.*;
import qna.CannotDeleteException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuestionTest {
    public static final Question QUESTION_OF_JAVAJIGI = new Question(1L, "title1", "contents1")
            .writeBy(UserTest.USER_JAVAJIGI);

    public static final Question QUESTION_OF_SANJIGI = new Question(2L, "title2", "contents2")
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
    @DisplayName("질문 삭제시 직접 입력한 질문과 답변일시 모두 삭제")
    void deleted() throws CannotDeleteException {
        QUESTION_OF_JAVAJIGI.addAnswer(AnswerTest.ANSWER_OF_JAVAJIGI);

        QUESTION_OF_JAVAJIGI.deleted(UserTest.USER_JAVAJIGI);

        assertAll(
            () -> assertThat(QUESTION_OF_JAVAJIGI.isDeleted()).isTrue(),
            () -> assertThat(AnswerTest.ANSWER_OF_JAVAJIGI.isDeleted()).isTrue(),
            () -> assertThat(AnswerTest.ANSWER_OF_SANJIGI.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문 삭제시 직접 입력한 질문이고, 다른사람의 답변이 달려있을경우 에러 발생")
    void CannotDeleteException() {
        Answer otherPeopleAnswer = Answer.builder()
                .writer(UserTest.USER_SANJIGI)
                .build();

        otherPeopleAnswer.toQuestion(QUESTION_OF_JAVAJIGI);

        assertThrows(CannotDeleteException.class, () -> {
            QUESTION_OF_JAVAJIGI.deleted(UserTest.USER_JAVAJIGI);
        });
    }

}