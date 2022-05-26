package qna.domain;

import static org.assertj.core.api.Assertions.*;

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
        삭제성공();
    }

    private void 삭제성공() {
        assertThat(question.isDeleted()).isTrue();
    }


    @Test
    void 로그인유저와_질문자가_다른경우_질문삭제불가(){
        ThrowingCallable tryDelete = () -> {
            question.deleteByUser(sanjigi);
        };
        삭제_불가능_예외가_발생한다(tryDelete);
    }

    private void 삭제_불가능_예외가_발생한다(ThrowingCallable tryDelete) {
        assertThatThrownBy(tryDelete).isInstanceOf(CannotDeleteException.class);
    }


}
