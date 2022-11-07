package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("delete_history 엔티티 테스트")
public class DeleteHistoryTest extends TestBase {
    public static final DeleteHistory DELETE_QUESTION_HISTORY = new DeleteHistory(ContentType.QUESTION, 1L,
            UserTest.MINGVEL);
    public static final DeleteHistory DELETE_ANSWER_HISTORY = new DeleteHistory(ContentType.ANSWER, 2L,
            UserTest.MINGVEL);

    @DisplayName("save 성공")
    @Test
    void save_deleteHistory_success() {
        //given:
        final User user = userRepository.save(UserTest.MINGVEL);
        //when:
        DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.QUESTION, 1L, user));
        //then:
        assertThat(deleteHistory.getDeletedByUser()).isEqualTo(user);
    }
}
