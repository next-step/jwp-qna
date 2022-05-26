package qna.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("Answer 객체 deleted 변경 테스트")
    @Test
    void deleteAnswer() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "ABC");
        answer.delete();

        assertThat(answer.isDeleted()).isTrue();
    }

}
