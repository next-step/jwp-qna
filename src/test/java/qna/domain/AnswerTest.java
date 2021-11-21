package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, Contents.of("Answers Contents1"));
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, Contents.of("Answers Contents2"));

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", Contents.of("contents1")).writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, Contents.of("Answers Contents1"));
    }

    @Test
    @DisplayName("답변이 생기면 질문에도 답변이 생겨야한다.")
    void toQuestionTest() {
        assertThat(question.getAnswers()).isEqualTo(Answers.of(answer));
    }

    @Test
    void 질문_삭제시_답변_삭제상태_변경_되어야한다() throws Exception {
        question.delete(UserTest.JAVAJIGI);

        assertThat(answer.isDeleted()).isTrue();
    }
}