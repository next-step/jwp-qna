package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswersTest {

    @Test
    void delete() throws CannotDeleteException {
        // given
        final User writer = TestUserFactory.create(
            1L, "javajigi", "password", "name", "javajigi@slipp.net"
        );
        final Question question = TestQuestionFactory.create(1L, "title1", "contents1", writer);
        final Answer answer1 = TestAnswerFactory.create(1L, writer, question, "Answers Contents1");
        final Answer answer2 = TestAnswerFactory.create(2L, writer, question, "Answers Contents2");
        final Answers answers = new Answers(Arrays.asList(answer1, answer2));

        // when
        final List<DeleteHistory> deleteHistories = answers.delete(writer);

        // then
        assertThat(deleteHistories).hasSize(2);
    }
}
