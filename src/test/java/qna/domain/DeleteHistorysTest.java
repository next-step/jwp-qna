package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistorysTest {
    @DisplayName("삭제이력 생성")
    @Test
    void init() {
        DeleteHistorys deleteHistorys = new DeleteHistorys(Arrays.asList(new DeleteHistory()));

        assertThat(deleteHistorys).isNotNull();
    }

    @DisplayName("삭제이력 합친 후 크기 확인")
    @Test
    void prepend() {
        User user = TestCreateFactory.createUser(1L);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user, LocalDateTime.now());
        DeleteHistorys deleteHistorys = new DeleteHistorys(Arrays.asList(deleteHistory));
        DeleteHistorys prepend = deleteHistorys.prepend(deleteHistory);

        int size = prepend.values().size();

        assertThat(size).isEqualTo(2);
    }
}
