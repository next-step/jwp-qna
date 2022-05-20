package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.FixtureUser.HEOWC;
import static qna.domain.FixtureUser.JAVAJIGI;

@DisplayName("Answer 클래스 테스트")
class AnswerTest {

    private Question question;

    @BeforeEach
    void setup() {
        question = new Question("answer", "answer").writeBy(JAVAJIGI);
    }

    @DisplayName("질문의 주인이 아닌 유저가 질문을 지우려 하면 예외 발생")
    @Test
    void failureDelete() {
        final Answer answer = new Answer(JAVAJIGI, question, "answer");
        assertThatThrownBy(() -> {
            answer.delete(HEOWC);
        })
        .isInstanceOf(CannotDeleteException.class)
        .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("질문 제거 성공")
    @Test
    void successfulDelete() {
        final Answer answer = new Answer(HEOWC, question, "answer");
        answer.delete(HEOWC);
        assertThat(answer.isDeleted()).isTrue();
    }
}