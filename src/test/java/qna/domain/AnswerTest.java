package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class AnswerTest {
    
    private User userA;
    private User userB;

    private Answer answerFromA;

    @BeforeEach
    void setUp() {
        userA = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        userB = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        Question questionFromB = new Question("title2", "contents2").writeBy(userB);

        answerFromA = new Answer(100L, userA, questionFromB, "JPA Answer");
    }

    @DisplayName("로그인 사용자와 답변한 사람이 다른 경우, 답변 삭제 불가능 해야 한다")
    @Test
    void delete_differentUser() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answerFromA.delete(userB))
            .withMessageContaining("답변을 삭제할 권한이 없습니다");
    }

    @DisplayName("로그인 사용자와 답변한 사람이 같은 경우, 답변만 삭제 가능 해야 한다")
    @Test
    void delete_sameUser() throws CannotDeleteException {
        assertThat(answerFromA.isDeleted()).isFalse();

        final DeleteHistory deleteHistory = answerFromA.delete(userA);
        assertThat(answerFromA.isDeleted()).isTrue();
        assertThat(deleteHistory).isEqualTo(
            new DeleteHistory(ContentType.ANSWER, 100L, userA, LocalDateTime.now()));
    }


}
