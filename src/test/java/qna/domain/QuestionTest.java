package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.ErrorMessage;
import qna.UnAuthorizedException;

public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("유저정보가 null인 Question 생성시 예외")
    void given_userNull_then_UnAuthorizedException() {
        Assertions.assertThatThrownBy(() -> new Question(3L, "title3", "content3").writeBy(null))
                  .isInstanceOf(UnAuthorizedException.class)
                  .hasMessage(ErrorMessage.USER_IS_NOT_NULL);
    }

    @Test
    @DisplayName("삭제된 데이터 검증")
    void given_Q1_when_Change_Delete_To_true_then_isTrue() throws CannotDeleteException {
        // given
        User user = new User("seunghoona", "password", "username", "email");
        Question question = new Question("title", "contents").writeBy(user);

        // when
        List<DeleteHistory> delete = question.delete(user);

        // then
        assertThat(delete).isEqualTo(new DeleteHistory(QUESTION, question, user, LocalDateTime.now()));
    }

    @Test
    @DisplayName("유저 이메일이 변경되는지 확인")
    void give_User_when_changeEmail_then_changedEqualsEmail() {
        // given
        User user = new User("seunghoona", "password", "username", "email");

        // when
        final String email = "seunghoo@naver.com";
        user.changeEmail(email);

        // then
        assertThat(user.getEmail()).isEqualTo(email);
    }
}
