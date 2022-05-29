package qna.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "This is apple.");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q2, "Wryyyyyyyyyyyyyyy!!!!");

    @DisplayName("Answer 객체 기본 API 테스트")
    @Test
    void answerNormal() {
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.JAVAJIGI)).isFalse(),
                () -> assertThat(A1.getWriter()).isEqualTo(UserTest.JAVAJIGI),
                () -> assertThat(A2.isDeleted()).isFalse()
        );
    }

    @DisplayName("Answer 객체 delete 테스트")
    @Test
    void deleteAnswer() {
        User writer = new User(30L, "onepunch", "abcd", "ho9", "abc@gmail.com");
        Answer answer = new Answer(writer, QuestionTest.Q1, "ABC");
        answer.delete(writer, LocalDateTime.now());

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("Answer 객체 delete 를 writer 와 다른 유저가 동작시 Exception 발생 확인")
    @Test
    void deleteAnswerByOtherUser() {
        User writer = new User(30L, "onepunch", "abcd", "ho9", "abc@gmail.com");
        Answer answer = new Answer(writer, QuestionTest.Q1, "ABC");
        User other = new User(31L, "Jonson", "vill", "ho8", "abbb@gmail.com");

        assertThatThrownBy(() -> answer.delete(other, LocalDateTime.now())).isInstanceOf(CannotDeleteException.class);
    }

}
