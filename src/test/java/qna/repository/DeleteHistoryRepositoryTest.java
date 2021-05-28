package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistories;

    @DisplayName("저장하기")
    @Test
    void save() {
        User deletedBy = new User("testUserId", "testPassword", "testName", "test@email.com");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, deletedBy);

        DeleteHistory saveDeleteHistory = deleteHistories.save(deleteHistory);

        assertThat(saveDeleteHistory).isEqualTo(deleteHistory);
        assertThat(saveDeleteHistory).isSameAs(deleteHistory);
    }
}
