package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void 객체_생성_유저_null일경우_UnAuthorizedException() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents"))
            .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 객체_생성_질문_null일경우_UnAuthorizedException() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents"))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 동등성() {
        assertThat(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents"))
            .isEqualTo(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents"));

        assertThat(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents"))
            .isNotEqualTo(new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents"));
    }

    @Test
    void 작성자_확인() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @Test
    void 질문_변경() {
        Question question1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        Question question2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, question1, "Answers Contents");

        answer.toQuestion(question2);
        assertThat(answer.getQuestionId()).isNotEqualTo(question1.getId());
        assertThat(answer.getQuestionId()).isEqualTo(question2.getId());
    }
}
