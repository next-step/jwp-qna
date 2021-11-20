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


    @DisplayName("Answer 값 확인")
    @Test
    void init() {
        User user = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(user);
        Answer answer = TestCreateFactory.createAnswer(user, question);

        assertThat(answer.getWriter()).isEqualTo(user);
    }

    @DisplayName("로그인한 사용자의 답변 삭제")
    @Test
    void deleteAnswer() {
        User loginUser = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(loginUser);
        Answer answer = TestCreateFactory.createAnswer(loginUser, question);

        answer.delete(loginUser);

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 로그인한 사용자의 답변이 아닐 경우")
    @Test
    void invalidDeleteAnswer() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                User loginUser = TestCreateFactory.createUser(1L);
                User answerUser = TestCreateFactory.createUser(2L);
                Question question = TestCreateFactory.createQuestion(loginUser);
                Answer answer = TestCreateFactory.createAnswer(answerUser, question);

                answer.delete(loginUser);
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
