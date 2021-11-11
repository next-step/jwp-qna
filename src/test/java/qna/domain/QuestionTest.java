package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Question 테스트")
public class QuestionTest {

    public static final Question TWO_ANSWERED_QUESTION = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question ONE_ANSWERED_QUESTION = new Question("title2", "contents2").writeBy(UserTest.JAVAJIGI);
    public static final Question NOT_ANSWERED_QUESTION = new Question("title3", "contents3").writeBy(UserTest.JAVAJIGI);

    @BeforeEach
    void setUp() {
        TWO_ANSWERED_QUESTION.addAnswer(AnswerTest.A1);
        TWO_ANSWERED_QUESTION.addAnswer(AnswerTest.A2);
        ONE_ANSWERED_QUESTION.addAnswer(AnswerTest.A3);
    }

    @Test
    @DisplayName("답변이 없는 질문을 삭제한다.")
    void delete1() {
        // when
        NOT_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI);

        // then
        assertThat(NOT_ANSWERED_QUESTION.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변이 있는 질문을 삭제하면 답변도 삭제한다.")
    void delete2() {
        // when
        ONE_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI);

        // then
        assertAll(
                () -> assertThat(ONE_ANSWERED_QUESTION.isDeleted()).isTrue(),
                () -> assertThat(AnswerTest.A3.isDeleted()).isTrue()
        );
    }

    @Test
    @DisplayName("질문을 삭제하면 삭제 이력 리스트를 반환한다.")
    void delete3() {
        // when
        DeleteHistories deleteHistories = ONE_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI);

        // then
        assertThat(deleteHistories.getDeleteHistories()).containsExactly(
                new DeleteHistory(ContentType.QUESTION, ONE_ANSWERED_QUESTION.getId(), ONE_ANSWERED_QUESTION.getWriter()),
                new DeleteHistory(ContentType.ANSWER, AnswerTest.A3.getId(), AnswerTest.A3.getWriter())
        );
    }

    @Test
    @DisplayName("질문자가 아닌 사용자가 질문을 삭제하면 예외가 발생한다.")
    void deleteThrowException1() {
        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> TWO_ANSWERED_QUESTION.delete(UserTest.SANJIGI))
                .withMessageMatching(ErrorMessage.DELETE_QUESTION_NOT_ALLOWED.getMessage());
    }

    @Test
    @DisplayName("질문자에 다른 사람의 답변이 존재하면 예외가 발생한다.")
    void deleteThrowException2() {
        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> TWO_ANSWERED_QUESTION.delete(UserTest.JAVAJIGI))
                .withMessageMatching(ErrorMessage.EXISTS_ANSWER_OF_OTHER.getMessage());
    }
}
