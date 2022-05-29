package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.util.assertions.QnaAssertions.삭제불가_예외발생;
import static qna.util.assertions.QnaAssertions.질문삭제여부_검증;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;

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
    void 로그인유저와_질문자가_동일한경우_삭제가능() throws CannotDeleteException{
        로그인유저와_질문자가_동일한경우_삭제시도();
        질문삭제여부_검증(question);
    }

    @Test
    void 로그인유저와_질문자가_다른경우_삭제불가(){
        ThrowingCallable tryDelete = () -> {
            로그인유저와_질문자가_다른경우_삭제시도();
        };
        인가예외발생(tryDelete);
    }

    private void 로그인유저와_질문자가_동일한경우_삭제시도() throws CannotDeleteException{
        question.delete(javajigi);
    }

    private void 로그인유저와_질문자가_다른경우_삭제시도() throws CannotDeleteException{
        question.delete(sanjigi);
    }

    public static void 인가예외발생(ThrowingCallable 삭제시도){
        assertThatThrownBy(삭제시도).isInstanceOf(UnAuthorizedException.class);
    }
}
