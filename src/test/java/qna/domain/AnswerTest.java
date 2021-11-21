package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {

    public static final Answer A1 =
        new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 =
        new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void equals() {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question question = TestQuestionFactory.create(1L, "title1", "contents1", writer);
        final Answer before = TestAnswerFactory.create(1L, writer, question, "Answers Contents1");
        final Answer after = TestAnswerFactory.create(1L, writer, question, "Answers Contents1-1");

        // when, then
        assertThat(before).isEqualTo(after);
    }

    @Test
    void delete() {
        // given
        final User owner = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final User someUser = TestUserFactory.create(
            2L, "sanjigi", "password", "name", "sanjigi@slipp.net"
        );
        final Question question = TestQuestionFactory.create(1L, "title1", "contents1", owner);
        final Answer answer = TestAnswerFactory.create(1L, owner, question, "Answers Contents1-1");

        // when, then
        assertAll(
            () -> assertDoesNotThrow(
                () -> answer.delete(owner)
            ),
            () -> assertThatThrownBy(
                () -> answer.delete(someUser)
            ).isInstanceOf(CannotDeleteException.class)
        );
    }
}
