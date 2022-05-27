package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.exception.UnAuthorizedException;

public class AnswerTest {
    private User javajigi;
    private User sanjigi;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp(){
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(javajigi);
        answer = new Answer(javajigi,question,"Answer1");
    }

    @Test
    void 로그인유저와_답변작성자가_동일한경우_삭제가능(){
        answer.delete(javajigi);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    void 로그인유저와_답변작성자가_다른경우_삭제불가(){
        assertThatThrownBy(()->{answer.delete(sanjigi);}).isInstanceOf(UnAuthorizedException.class);
    }
}
