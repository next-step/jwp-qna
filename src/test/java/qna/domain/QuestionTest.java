package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);

    @Test
    void equals() {
        // given
        final User writer = TestUserFactory.create(
            "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question before = TestQuestionFactory.create(1L, "title1", "contents1", writer);
        final Question after = TestQuestionFactory.create(1L, "title1-1", "contents1-1", writer);

        // when, then
        assertThat(before).isEqualTo(after);
    }

    @Test
    void addAnswer() {
        // given
        final User writer = TestUserFactory.create(
            "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question question = TestQuestionFactory.create("title1", "contents1", writer);
        final Answer answer = TestAnswerFactory.create(writer, question, "Answers Contents1");

        // when
        question.addAnswer(answer);

        // then
        assertThat(answer.getQuestion()).isEqualTo(question);
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

        // when, then
        assertAll(
            () -> assertDoesNotThrow(
                () -> question.delete(owner)
            ),
            () -> assertThatThrownBy(
                () -> question.delete(someUser)
            ).isInstanceOf(CannotDeleteException.class)
        );
    }
}
