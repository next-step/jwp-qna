package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class AnswersTest {

    @Test
    void delete() throws CannotDeleteException {
        Answers answers = new Answers(singletonList(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1")));

        List<DeleteHistory> deleteHistories = answers.delete(UserTest.JAVAJIGI);
        assertThat(deleteHistories).contains(DeleteHistory.ofAnswer(1L, UserTest.JAVAJIGI));
    }
}
