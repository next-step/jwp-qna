package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("정상적인 삭제 테스트")
    void delete() throws CannotDeleteException {
        DeleteHistories delete = Q1.delete(UserTest.JAVAJIGI);

        Assertions.assertThat(delete.getDeleteHistories()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("다른 유저가 삭제를 시도한 경우 실패 테스트")
    void cannotDeleteException() {
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .withMessageContaining("답변을 삭제할 수 없습니다.");
    }
}
