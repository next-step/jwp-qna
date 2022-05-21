package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.FixtureUser.HEOWC;
import static qna.domain.FixtureUser.JAVAJIGI;

@DisplayName("Question 클래스 테스트")
class QuestionTest {

    @DisplayName("질문의 주인이 아닌 유저가 질문을 지우려 하면 예외 발생")
    @Test
    void failureDelete() {
        final Question question = new Question("question", "question").writeBy(JAVAJIGI);
        assertThatThrownBy(() -> {
            question.delete(HEOWC);
        })
        .isInstanceOf(CannotDeleteException.class)
        .hasMessageContaining("질문을 삭제할 권한이 없습니다.");
    }

    @DisplayName("질문 제거 성공")
    @Test
    void successfulDelete() {
        final Question question = new Question("question", "question").writeBy(HEOWC);
        question.delete(HEOWC);
        assertThat(question.isDeleted()).isTrue();
    }
}
