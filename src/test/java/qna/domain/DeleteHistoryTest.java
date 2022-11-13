package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static qna.domain.AnswerTest.ANSWER_1;
import static qna.domain.UserTest.JAVAJIGI;

@DisplayName("삭제 내역")
public class DeleteHistoryTest {

    @DisplayName("질문 삭제 내역 생성")
    @Test
    void constructor_ofQuestion() {
        assertThatNoException().isThrownBy(() -> DeleteHistory.ofQuestion(ANSWER_1.getId(), JAVAJIGI));
    }

    @DisplayName("답변 삭제 내역 생성")
    @Test
    void constructor_ofAnswer() {
        assertThatNoException().isThrownBy(() -> DeleteHistory.ofAnswer(ANSWER_1.getId(), JAVAJIGI));
    }
}
