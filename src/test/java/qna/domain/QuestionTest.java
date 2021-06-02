package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final User user1 = new User(1L, "user1", "password", "user1", "user1@test.com");
    public static final User user2 = new User(2L, "user2", "password", "user2", "user2@test.com");
    public static final Question Q1 = new Question("title1", "contents1").writeBy(user1);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(user2);

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        @Nested
        @DisplayName("주어진 질문 정보와 동일한 사용자가 주어지면")
        class Context_with_question_and_same_user {
            final Question givenQuestion = Q1;
            final User compareUser = user1;

            @Test
            @DisplayName("삭제되었다고 표시한다")
            void it_returns_true() {
                givenQuestion.delete(compareUser);
                assertThat(givenQuestion.isDeleted()).isTrue();
            }
        }

        @Nested
        @DisplayName("주어진 질문 정보와 다른 사용자가 주어지면")
        class Context_with_question_and_different_user {
            final Question givenQuestion = Q1;
            final User compareUser = user2;

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> givenQuestion.delete(compareUser))
                        .isInstanceOf(CannotDeleteException.class);
            }
        }
    }

    @DisplayName("질문 작성자를 등록하고 작성자를 리턴한다.")
    @Test
    void registerWriter() {
        // given
        Question givenQuestion = new Question("title1", "contents1");

        // when
        Question question = givenQuestion.writeBy(user1);

        // then
        assertThat(question.getWriter()).isNotNull();
    }
}
