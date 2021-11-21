package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 생성")
    @Test
    void init() {
        User user = TestCreateFactory.createUser(1L);
        Question question = TestCreateFactory.createQuestion(user);

        assertThat(question).isNotNull();
    }

    @DisplayName("로그인한 사용자가 작성한 질문 삭제")
    @Test
    void deleteQuestion() {
        User loginUser = TestCreateFactory.createUser(1L);
        Question loginUserQuestion = TestCreateFactory.createQuestion(loginUser);
        loginUserQuestion.delete(loginUser, LocalDateTime.of(2021,12,13,5,0));

        boolean isDeleted = loginUserQuestion.isDeleted();

        assertThat(isDeleted).isTrue();
    }

    @DisplayName("로그인한 사용자가 다른사람의 질문을 삭제할 경우")
    @Test
    void invalidDeleteQuestion() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                User loginUser = TestCreateFactory.createUser(1L);
                User anotherUser = TestCreateFactory.createUser(2L);
                Question anotherQuestion = TestCreateFactory.createQuestion(anotherUser);

                anotherQuestion.delete(loginUser, LocalDateTime.now());
            }).withMessageMatching("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("로그인한 사용자의 질문, 답변 모두 삭제")
    @Test
    void deleteQuestionContainsAnswers() throws CannotDeleteException {
        User loginUser = TestCreateFactory.createUser(1L);
        Question loginUserQuestion = TestCreateFactory.createQuestion(loginUser);
        Answer loginUserAnswer = TestCreateFactory.createAnswer(loginUser, loginUserQuestion);
        loginUserQuestion.addAnswer(loginUserAnswer);
        loginUserQuestion.delete(loginUser, LocalDateTime.now());

        boolean isDeleted = loginUserQuestion.isDeleted();

        assertThat(isDeleted).isTrue();
    }

    @DisplayName("다른사용자의 답변이 포함된 질문 삭제")
    @Test
    void invalidDeleteQuestionContainsAnotherUserAnswers() throws CannotDeleteException {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> {
                User loginUser = TestCreateFactory.createUser(1L);
                User anotherUser = TestCreateFactory.createUser(2L);
                Question loginUserQuestion = TestCreateFactory.createQuestion(loginUser);
                Answer anotherUserAnswer = TestCreateFactory.createAnswer(anotherUser, loginUserQuestion);
                loginUserQuestion.addAnswer(anotherUserAnswer);

                loginUserQuestion.delete(loginUser, LocalDateTime.now());
            }).withMessageMatching("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
