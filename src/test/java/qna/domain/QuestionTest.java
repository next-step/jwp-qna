package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question javaJigiQuestion;
    private User javaJigi;
    private User sanJigi;
    private Answer javaJigiAnswer;
    private Answer sanJigiAnswer;

    @BeforeEach
    void setUp() {
        javaJigi = UserTest.JAVAJIGI;
        sanJigi = UserTest.SANJIGI;
        javaJigiAnswer = AnswerTest.A1;
        sanJigiAnswer = AnswerTest.A2;
    }

    @DisplayName("Question 값 확인")
    @Test
    void init() {
        javaJigiQuestion = new Question("title1", "contents1").writeBy(javaJigi);

        assertAll(
            () -> assertThat(javaJigiQuestion.getTitle()).isEqualTo("title1"),
            () -> assertThat(javaJigiQuestion.getContents()).isEqualTo("contents1"),
            () -> assertThat(javaJigiQuestion.getWriter()).isEqualTo(javaJigi)
        );
    }

    @DisplayName("질문 삭제")
    @Test
    void deleteQuestion() throws CannotDeleteException {
        javaJigiQuestion = new Question("title1", "contents1").writeBy(javaJigi);
        javaJigiQuestion.delete(javaJigi);

        boolean isDeleted = javaJigiQuestion.isDeleted();

        assertThat(isDeleted).isTrue();
    }

    @DisplayName("삭제. 질문자와 로그인한 사용자가 다를 경우 오류")
    @Test
    void invalidDeleteQuestion() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                javaJigiQuestion = new Question("title1", "contents1").writeBy(javaJigi);

                javaJigiQuestion.delete(sanJigi);
            }).withMessageMatching("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("답변을 포함한 질문을 삭제")
    @Test
    void deleteQuestionContainsAnswers() throws CannotDeleteException {
        javaJigiQuestion = new Question("title1", "contents1").writeBy(javaJigi);
        javaJigiQuestion.addAnswer(javaJigiAnswer);
        javaJigiQuestion.delete(javaJigi);

        boolean isDeleted = javaJigiQuestion.isDeleted();

        assertThat(isDeleted).isTrue();
    }
    @DisplayName("다른사용자의 답변이 포함된 질문 삭제")
    @Test
    void invalidDeleteQuestionContainsAnotherUserAnswers() throws CannotDeleteException {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                javaJigiQuestion = new Question("title1", "contents1").writeBy(javaJigi);
                javaJigiQuestion.addAnswer(javaJigiAnswer);
                javaJigiQuestion.addAnswer(sanJigiAnswer);

                javaJigiQuestion.delete(javaJigi);
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
