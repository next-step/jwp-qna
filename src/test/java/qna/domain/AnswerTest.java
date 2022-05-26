package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("답변 작성자 동일한지 확인")
    void isOwner() {
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @Test
    @DisplayName("질문 변경 후 질문 작성자 id 동일한지 확인")
    void toQuestion() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "answer contents");
        answer.toQuestion(QuestionTest.Q2);
        assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }
}
