package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private UserRepository users;

    @Autowired
    private DeleteHistoryRepository deleteHistorys;

    @DisplayName("저장 테스트")
    @Test
    void save() {
        User writer = users.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, writer);

        DeleteHistory result = deleteHistorys.save(deleteHistory);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContentType()).isEqualTo(deleteHistory.getContentType()),
                () -> assertThat(result.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(result.getDeletedByUser()).isEqualTo(deleteHistory.getDeletedByUser())
        );
    }
}
