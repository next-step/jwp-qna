package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    void delete_by_not_writer() {
        assertThrows(CannotDeleteException.class, () -> Q1.deleteBy(UserTest.SANJIGI));
    }

    @Test
    void delete_by_writer() throws CannotDeleteException {
        Q1.deleteBy(UserTest.JAVAJIGI);
        assertTrue(Q1.isDeleted());
    }
}
