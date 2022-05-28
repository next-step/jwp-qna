package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    private Answer answer1;

    @BeforeEach
    void before() {
        User user = new User(1L, "user1", "password", "name", "user1@com");

        Question question = new Question("title1", "contents1").writeBy(user);

        answer1 = new Answer(user, question, "Answers Contents1");
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
    void 답변_삭제에_대한_이력을_남긴다() {
        // given
        User writer = new User(1L, "user1", "password", "name", "user1@com");
        // when
        DeleteHistory deleteHistory = answer1.delete(writer);
        // then
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
    }
}
