package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class AnswersTest {

    @Test
    void add() {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question question1 = TestQuestionFactory.create(1L, "title1", "contents1", writer);
        final Answer answer1 = TestAnswerFactory.create(1L, writer, question1, "Answers Contents1");
        final Question question2 = TestQuestionFactory.create(2L, "title2", "contents2", writer);
        final Answer answer2 = TestAnswerFactory.create(2L, writer, question2, "Answers Contents2");

        // when, then
        assertAll(
            () -> assertThatThrownBy(
                () -> new Answers(Arrays.asList(answer1, answer1))
            ).isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(
                () -> new Answers(Arrays.asList(answer1, answer2))
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void delete() {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question question = TestQuestionFactory.create(1L, "title1", "contents1", writer);
        final Answer answer1 = TestAnswerFactory.create(1L, writer, question, "Answers Contents1");
        final Answer answer2 = TestAnswerFactory.create(2L, writer, question, "Answers Contents2");
        final Answers answers = new Answers(Arrays.asList(answer1, answer2));

        // when
        final DeleteHistories deleteHistories = answers.delete(writer);

        // then
        assertThat(deleteHistories.getValues()).hasSize(2);
    }
}
