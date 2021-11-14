package qna.deletehistory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.domain.ContentType;
import qna.domain.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DeleteHistoryTest {
    public static final DeleteHistory DH_ANSWER = new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
    public static final DeleteHistory DH_QUESTION = new DeleteHistory(2L, ContentType.QUESTION, 2L, UserTest.SANJIGI);

    @Test
    @DisplayName("삭제 이력 팩토리 메서드 생성")
    public void createDeleteHistoryTest() {
        assertThat(DH_ANSWER).isEqualTo(new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI));
    }

    @Test
    @DisplayName("삭제 이력 생성 실패 - null user")
    public void createDeleteHistoryTest_nullUser() {
        assertThatThrownBy(() -> new DeleteHistory(1L, ContentType.ANSWER, 1L, null))
                .isInstanceOf(NotFoundException.class);
    }
}
