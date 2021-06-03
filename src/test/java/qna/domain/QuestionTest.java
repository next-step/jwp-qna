package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User user;
    private User otherUser;
    private Question question;

    @BeforeEach
    void setUp() {
        //Given
        user = new User("AppleMango", "password", "name", "email@email.com");
        question = new Question("JPA Question", "Question Contents").writeBy(user);
        otherUser = new User("Conan", "password", "name", "conan@email.com");
    }

    @DisplayName("삭제 시, 질문 작성자와 삭제자가 다르면 exception을 던진다")
    @Test
    void throw_exception_when_other_user_is_deleting() {
        //When + Then
        assertThatThrownBy(() -> question.deleteBy(otherUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("삭제 시, 답변 작성자 중 한명이라도 질문 작성자와 다르면 exception을 던진다")
    @Test
    void throw_exception_when_answer_writer_is_different() {
        //Given
        Answer answerFromQuestionWriter = new Answer(user, question, "First Answer Contents");
        Answer answerFromOtherWriter = new Answer(otherUser, question, "Second Answer Contents");

        //When
        question.addAnswer(answerFromOtherWriter);
        question.addAnswer(answerFromQuestionWriter);

        //Then
        assertThatThrownBy(() -> question.deleteBy(user))
                .isInstanceOf(CannotDeleteException.class);
    }
}
