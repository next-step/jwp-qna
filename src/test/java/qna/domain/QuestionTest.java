package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    private Question question;

    @BeforeEach
    void before() {
        User user = new User(1L, "user1", "password", "name", "user1@com");

        question = new Question("title1", "contents1").writeBy(user);
    }

    @Test
    void 작성자를_설정한다() {
        Question question = new Question("title", "contents");
        User user = new User(2L, "user2", "password", "name", "user2@com");
        // when
        Question result = question.writeBy(user);
        // then
        assertThat(result.getWriter()).isEqualTo(user);
    }

    @Test
    void 내가_작성한_질문인지_확인한다() {
        // given
        User user = new User(1L, "user1", "password", "name", "user1@com");
        // when
        boolean result = question.isOwner(user);
        // then
        assertThat(result).isTrue();
    }
    
    @Test
    void 내가_작성한_질문만_삭제할_수_있다() {
        // given
        User user = new User(1L, "user1", "password", "name", "user1@com");
        Question question = new Question("title", "contents").writeBy(user);
        // when
        question.delete(user);
        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 다른_사람의_질문을_삭제할_경우_예외가_발생한다() {
        // given
        User user = new User(1L, "user1", "password", "name", "user1@com");
        Question question = new Question("title", "contents").writeBy(user);

        User loginUser = new User(2L, "user1", "password", "name", "user1@com");
        // when & then
        assertThatThrownBy(() ->
                question.delete(loginUser)
        ).isInstanceOf(CannotDeleteException.class);
    }
}
