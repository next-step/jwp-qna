package qna.domain;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "This is apple.");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q2, "Wryyyyyyyyyyyyyyy!!!!");

    @DisplayName("Answer 객체 기본 API 테스트")
    @Test
    private void answerNormal() {
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.JAVAJIGI)).isFalse(),
                () -> assertThat(A1.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(A2.isDeleted()).isFalse()
        );
    }

    @DisplayName("deleted 변경 테스트")
    @Test
    private void deleteAnswer() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "ABC");
        answer.setDeleted(true);

        assertThat(answer.isDeleted()).isTrue();
    }

}
