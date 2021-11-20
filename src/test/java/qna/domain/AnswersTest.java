package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {
    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents");
    }

    @DisplayName("답변 추가")
    @Test
    void add() {
        Answers answers = new Answers();
        Answer answer = new Answer();

        answers.add(answer);

        assertThat(answers).isEqualTo(new Answers(Arrays.asList(answer)));
    }

    @DisplayName("모든 답변자가 작성자인지 확인")
    @Test
    void validateOwner() {
        Answers answers = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));

        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answers.validateOwner(UserTest.JAVAJIGI))
            .withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}