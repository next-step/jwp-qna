package qna.domain.question;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.domain.user.User;
import qna.domain.user.UserTest;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User owner;
    private Question question;

    @BeforeEach
    void setUp() {
        owner = new User(3L, "len", "password", "name", "len@gmail.com");
        question = new Question(1L, "title1", "contents2").writeBy(owner);
    }

    @Test
    void Owner가_아닌_사람이_질문을_삭제하면_예외를_출력() {
        User notOwnerUser = UserTest.JAVAJIGI;

        assertThat(question.isDeleted()).isFalse();
        assertThatThrownBy(() -> question.markDeleteWhenUserOwner(notOwnerUser))
            .isInstanceOf(CannotDeleteException.class)
        .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void Owner가_질문을_삭제하면_mark_가_표시된다() throws Exception {
        assertThat(question.isDeleted()).isFalse();

        question.markDeleteWhenUserOwner(owner);

        assertThat(question.isDeleted()).isTrue();
    }
}
