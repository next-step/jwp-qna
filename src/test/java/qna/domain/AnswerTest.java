package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, Q1, "Answers Contents2");

    @DisplayName("changeQuestion 을 여러번 호출하면 기존 question 의 answer 는 지워진다.")
    @Test
    void changeQuestion() {
        // given
        Question question1 = new Question("question1", "questionContents1");
        Question question2 = new Question("question2", "questionContents2");
        Answer answer = new Answer(UserTest.JAVAJIGI, question1, "Contents1");

        // when
        answer.changeQuestion(question2);

        // then
        assertEquals(question2, answer.getQuestion());
        assertEquals(1, answer.getQuestion().getAnswers().size());
        assertThat(answer.getQuestion().getAnswers()).containsExactly(answer);
        assertEquals(0, question1.getAnswers().size());
    }
}
