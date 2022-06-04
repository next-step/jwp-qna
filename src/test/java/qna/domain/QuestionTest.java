package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문의 작성자를 확인한다.")
    void checkValidWriter() {
        assertAll(
            () -> {
                assertThat(Q1.getUser().getId()).isEqualTo(UserTest.JAVAJIGI.getId());
                assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
            },
            () -> {
                assertThat(Q2.getUser().getId()).isEqualTo(UserTest.SANJIGI.getId());
                assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue();
            }
        );
    }

    @Test
    @DisplayName("질문에 답변을 작성할 수 있다.")
    void checkAddAnswer() {
        assertAll(
            () -> {
                Q1.addAnswer(AnswerTest.A1);
                assertThat(AnswerTest.A1.getQuestionId()).isEqualTo(Q1.getId());
            },
            () -> {
                Q2.addAnswer(AnswerTest.A2);
                assertThat(AnswerTest.A2.getQuestionId()).isEqualTo(Q2.getId());
            }
        );
    }
}
