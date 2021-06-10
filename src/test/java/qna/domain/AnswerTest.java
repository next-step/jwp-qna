package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    void delete_by_not_writer() {
        assertThrows(CannotDeleteException.class, () -> A1.deleteBy(UserTest.SANJIGI));
    }

    @Test
    void delete_by_writer() throws CannotDeleteException {
        A1.deleteBy(UserTest.JAVAJIGI);
        assertTrue(A1.isDeleted());
    }
}
