package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.UserTest;
import qna.domain.content.ContentType;
import qna.domain.history.DeleteHistory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("삭제 이력 테스트")
class DeleteHistoryTest {

    @Test
    void user_동등성_테스트() {
        assertAll(
                () -> assertEquals(
                        new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI),
                        new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI)
                ),
                () -> assertNotEquals(
                        new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI),
                        new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI)
                )
        );
    }

}
