package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class AnswerTest {

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
        final Question question = TestQuestionFactory.create(1L, "title1", "contents1", owner);
        final Answer answer = TestAnswerFactory.create(1L, owner, question, "Answers Contents1");
        answer.delete(owner);

        // when
        final DeleteHistories deleteHistories = question.delete(owner);

        // then
        assertThat(deleteHistories.getValues()).hasSize(1);
    }

    @Test
    void delete_invalidOwner() {
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
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }
}
