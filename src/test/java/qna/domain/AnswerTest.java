package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void before() {
        User user1 = new User(1L, "user1", "password", "name", "user1@com");
        User user2 = new User(2L, "user2", "password", "name", "user2@com");

        Question question1 = new Question("title1", "contents1").writeBy(user1);
        Question question2 = new Question("title2", "contents2").writeBy(user2);

        answer1 = new Answer(user1, question1, "Answers Contents1");
        answer2 = new Answer(user2, question2, "Answers Contents1");
    }

    @Test
    void 작성자가_null일_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                new Answer(1L, null, null, "test")
        ).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 질문이_null일_경우_예외가_발생한다() {
        // given
        User user = new User(1L, "user1", "password", "name", "user1@com");
        // when & then
        assertThatThrownBy(() ->
                new Answer(1L, user, null, "test")
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void 내가_작성한_댓글인지_확인한다() {
        // given
        User user = new User(1L, "user1", "password", "name", "user1@com");
        // when
        boolean result = answer1.isOwner(user);
        // then
        assertThat(result).isTrue();
    }

    @Test
    void 질문을_설정한다() {
        // given
        Question question = new Question(2L, "title", "contents");
        // when
        answer2.toQuestion(question);
        // then
        assertThat(answer2.getQuestionId()).isEqualTo(question.getId());
    }
}
