package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question testQ1;

    private Question testQ2WithAnswer;

    @BeforeEach
    void setUp() {
        testQ1 = new Question("title test 1", "contents test 1").writeBy(UserTest.JAVAJIGI);
        testQ2WithAnswer = new Question("title test 2", "content test 2").writeBy(UserTest.SANJIGI);
        testQ2WithAnswer.addAnswer(new Answer(UserTest.JAVAJIGI, testQ2WithAnswer, "answer test 2"));
    }

    @Test
    void 답변이_없는_질문_삭제() throws CannotDeleteException {
        User loginUser = testQ1.getWriter();
        testQ1.delete(loginUser);
        assertTrue(testQ1.isDeleted());
    }

    @Test
    void 질문_삭제_권한이_없는_경우() {
        User anotherUser = UserTest.SANJIGI;
        assertThrows(CannotDeleteException.class, () -> testQ1.delete(anotherUser)
                , "질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 이미_삭제된_질문을_다시_삭제_시도() throws CannotDeleteException {
        User loginUser = testQ1.getWriter();
        testQ1.delete(loginUser);
        assertThrows(CannotDeleteException.class, () -> testQ1.delete(loginUser)
                , "이미 삭제된 질문입니다.");
    }

    @Test
    void 다른_사용자가_답변을_한_경우_삭제_불가() {
        User loginUser = UserTest.SANJIGI;
        assertThrows(CannotDeleteException.class, () -> testQ2WithAnswer.delete(loginUser)
                , "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

}
