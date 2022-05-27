package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.assertions.QnaAssertions.*;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;

public class QuestionTest {

    private User javajigi;
    private User sanjigi;
    private Question question;

    @BeforeEach
    void setUp(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(javajigi);
    }

    @Test
    void 로그인유저와_질문자가_동일한경우_질문삭제가능() throws CannotDeleteException{
        question.deleteByUser(javajigi);
        질문삭제여부_검증(question);
    }

    @Test
    void 로그인유저와_질문자가_다른경우_질문삭제불가(){
        ThrowingCallable tryDelete = () -> {
            question.deleteByUser(sanjigi);
        };
        삭제불가_예외발생(tryDelete);
    }
}
