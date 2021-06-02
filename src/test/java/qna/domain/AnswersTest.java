package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {
    private User user1 = new User(1L, "tester", "password", "tester", "test@test.com");
    private User user2 = new User(2L, "tester2", "password", "tester2", "test2@test.com");
    private Question question1 = new Question("title", "content");
    private Answer answer1 = new Answer(user1, question1, "content");
    private Answer answer2 = new Answer(user2, question1, "content2");

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        Answers answers = new Answers();

        @Nested
        @DisplayName("주어진 답변 정보와 동일한 사용자가 주어지면")
        class Context_with_question_and_same_user {
            final Answer givenAnswer = answer1;
            final User givenUser = user1;

            @BeforeEach
            void setUp() {
                answers.add(givenAnswer);
            }

            @Test
            @DisplayName("삭제이력 목록을 리턴한다.")
            void it_returns_delete_histories() {
                DeleteHistories actual = answers.delete(givenUser);

                assertThat(actual.size()).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("주어진 삭제 정보와 다른 사용자가 주어지면")
        class Context_with_question_and_different_user {
            final Answer givenAnswer = answer1;
            final User givenUser = user2;

            @BeforeEach
            void setUp() {
                answers.add(givenAnswer);
            }

            @Test
            @DisplayName("예외를 던진다")
            void it_throws_exception() {
                assertThatThrownBy(() -> answers.delete(givenUser))
                        .isInstanceOf(CannotDeleteException.class);
            }
        }
    }

}
