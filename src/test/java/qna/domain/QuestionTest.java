package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("Question 객체 기본 API 테스트")
    @Test
    void questionNormal() {
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @DisplayName("Question 객체 delete 테스트")
    @Test
    void deleteQuestion() {
        Question question = new Question("title3", "contents3");
        User writer = new User("onepunch", "abc", "name", "abc@gmail.com");
        question.writeBy(writer);
        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("Question 객체 delete 를 writer 와 다른 유저가 동작시 Exception 발생 확인")
    @Test
    void deleteQuestionByOtherUser() {
        Question question = new Question("title3", "contents3");
        User writer = new User("onepunch", "abc", "name", "abc@gmail.com");
        question.writeBy(writer);
        User other = new User("hoho", "1234", "js", "att@gmail.com");

        assertThatThrownBy(() -> question.delete(other)).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("다른 유저가 작성한 Answer가 존재하는 Question 객체 delete 시 Exception 발생 확인")
    @Test
    void deleteQuestionWithOtherAnswer() {
        User writer = new User("onepunch", "abc", "name", "abc@gmail.com");
        User other = new User("hoho", "1234", "js", "att@gmail.com");

        Question question = new Question("title3", "contents3");
        question.writeBy(writer);

        Answer answerFirst = new Answer(writer, question, "abc");
        Answer answerSecond = new Answer(other, question, "att");
        question.addAnswer(answerFirst);
        question.addAnswer(answerSecond);

        assertThatThrownBy(() -> question.delete(writer)).isInstanceOf(CannotDeleteException.class);
    }
}
