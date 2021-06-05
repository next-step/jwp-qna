package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

    private final long id = 1L;
    private final User user = new User("id", "password", "name");

    @DisplayName("ofAnswer 정적 메소드로 생성한 객체가 예상과 일치히는지 테스트")
    @Test
    void ofAnswer() {
        // given
        final DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, id, user);

        // when
        final DeleteHistory actual = DeleteHistory.ofAnswer(id, user);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("ofQuestion 정적 메소드로 생성한 객체가 예상과 일치히는지 테스트")
    @Test
    void ofQuestion() {
        // given
        final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, id, user);

        // when
        final DeleteHistory actual = DeleteHistory.ofQuestion(id, user);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
