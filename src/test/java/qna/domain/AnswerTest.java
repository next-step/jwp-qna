package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {
    public static Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");
    private User javaJigi;
    private User sanJigi;
    private Question javaJigiQuestion;
    private Question sanJigiQuestion;

    @BeforeEach
    void setUp() {
        javaJigi = UserTest.JAVAJIGI;
        sanJigi = UserTest.SANJIGI;
        javaJigiQuestion = QuestionTest.Q1;
        sanJigiQuestion = QuestionTest.Q2;
    }

    @DisplayName("Answer 값 확인")
    @Test
    void init() {
        Answer JavaJigiAnswer = new Answer(javaJigi, javaJigiQuestion, "Answers Contents1");

        assertAll(
            () -> assertThat(JavaJigiAnswer.getWriter()).isEqualTo(javaJigi),
            () -> assertThat(JavaJigiAnswer.getQuestion()).isEqualTo(QuestionTest.Q1),
            () -> assertThat(JavaJigiAnswer.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @DisplayName("로그인한 사용자의 답변 삭제")
    @Test
    void deleteAnswer() throws CannotDeleteException {
        User loginUser = UserTest.JAVAJIGI;
        Answer answer = new Answer(loginUser, QuestionTest.Q1, "Answers Contents1");

        answer.delete(loginUser);

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 로그인한 사용자의 답변이 아닐 경우")
    @Test
    void invalidDeleteAnswer() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                User loginUser = UserTest.JAVAJIGI;
                User answerUser = UserTest.SANJIGI;
                Answer answer = new Answer(answerUser, QuestionTest.Q1, "Answers Contents1");

                answer.delete(loginUser);
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
