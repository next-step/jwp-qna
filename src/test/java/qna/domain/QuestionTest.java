package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

@DisplayName("질문에 대한 단위 테스트")
class QuestionTest {

    private User writer;
    private User other;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "woobeen", "password", "name", "drogba02@naver.com");
        other = new User(2L, "other", "password", "name", "other@naver.com");
    }

    @Test
    @DisplayName("질문을 작성한 자가 아닌 사람이 질문을 삭제하려 하면 예외가 발생한다")
    void delete_exception_test() {
        Question question = new Question("test-title", "test-contents").writeBy(writer);

        assertThatThrownBy(() -> {
            question.delete(other);
        }).isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문을 작성한 자가 질문을 삭제하려 하면 정상적으로 삭제되어야 한다")
    void delete_test() {
        Question question = new Question("test-title", "test-contents").writeBy(writer);
        question.delete(writer);

        assertTrue(question.isDeleted());
    }
}
