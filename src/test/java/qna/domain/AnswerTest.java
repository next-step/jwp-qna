package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    public static Answer createAnswer(Long id, User writer, Question question, boolean deleted) throws CannotDeleteException {
        Answer answer = new Answer(id, writer, question, "answer content1");
        if (deleted) {
            answer.delete(writer);
        }
        return answer;
    }

    public static Answer createAnswer(User writer, Question question, boolean deleted) throws CannotDeleteException {
        return createAnswer(null, writer, question, deleted);
    }

    @Test
    void delete() throws CannotDeleteException {
        Answer answer = createAnswer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, false);

        assertThat(answer.delete(UserTest.JAVAJIGI)).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void delete_본인답변이_아닌_경우() throws CannotDeleteException {
        Answer answer = createAnswer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, false);

        Assertions.assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> answer.delete(UserTest.SANJIGI));
    }
}
