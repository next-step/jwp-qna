package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");


    @DisplayName("답변 생성시 작성자가 없으면 UnAuthorizedException 발생시킨다")
    @Test
    void createAnswer_withoutWriter_exception() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("답변 생성시 질문이 없으면 NotFoundException 발생시킨다")
    @Test
    void createAnswer_withoutQuestion_exception() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("답변의 작성자가 주어진 작성자가 맞는지 확인할 수 있다")
    @Test
    void isOwner_test() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        assertAll(
                () -> assertTrue(answer.isOwner(UserTest.JAVAJIGI)),
                () -> assertFalse(answer.isOwner(UserTest.SANJIGI))
        );
    }

    @DisplayName("답변에 해당하는 질문을 변경할 수 있다")
    @Test
    void toQuestion_test() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answer.toQuestion(QuestionTest.Q2);
        assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }
}
