package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private Answer testA1;

    @BeforeEach
    void setUp() {
        testA1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    }

    @Test
    void 정상_삭제() throws CannotDeleteException {
        User loginUser = testA1.getWriter();
        testA1.delete(loginUser);
        assertTrue(testA1.isDeleted());
    }

    @Test
    void 답변_작성자가_아닌_사용자가_삭제() {
        User anotherUser = UserTest.SANJIGI;
        assertThrows(CannotDeleteException.class, () -> testA1.delete(anotherUser)
                , "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
