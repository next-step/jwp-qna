package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    //given

    //when

    //then
    @Test
    public void delete_작성자가_아닌_로그인유저() {
        //given
        User loginUser = UserTest.SANJIGI;
        //when
        //then
        assertThatThrownBy(() -> Q1.delete(loginUser)).isInstanceOf(
            CannotDeleteException.class);
    }

    @Test
    public void delete_작성자_로그인유저() throws CannotDeleteException {
        //given
        User loginUser = UserTest.JAVAJIGI;
        //when
        Q1.delete(loginUser);
        //then
        assertThat(Q1.isDeleted()).isTrue();
    }
}
