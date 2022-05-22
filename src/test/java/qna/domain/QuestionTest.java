package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
