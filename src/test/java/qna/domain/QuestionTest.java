package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Nested
    @DisplayName("isOwner 메서드는")
    class Describe_is_owner {
        @Nested
        @DisplayName("주어진 질문 정보와 동일한 사용자가 주어지면")
        class Context_with_question_and_same_user {
            final Question givenQuestion = Q1;
            final User compareUser = UserTest.JAVAJIGI;

            @Test
            @DisplayName("참을 리턴한다 ")
            void it_returns_true() {
                assertThat(givenQuestion.isOwner(compareUser)).isTrue();
            }
        }

        @Nested
        @DisplayName("주어진 질문 정보와 다른 사용자가 주어지면")
        class Context_with_question_and_different_user {
            final Question givenQuestion = Q1;
            final User compareUser = UserTest.SANJIGI;

            @Test
            @DisplayName("거짓을 리턴한다 ")
            void it_returns_false() {
                assertThat(givenQuestion.isOwner(compareUser)).isFalse();
            }
        }
    }

    @DisplayName("질문 작성자를 등록하고 작성자를 리턴한다.")
    @Test
    void registerWriter() {
        // given
        Question givenQuestion = new Question("title1", "contents1");

        // when
        Question question = givenQuestion.writeBy(UserTest.JAVAJIGI);

        // then
        assertThat(question.getWriter()).isNotNull();
    }
}
