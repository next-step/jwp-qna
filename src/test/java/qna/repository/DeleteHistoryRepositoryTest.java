package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.repository.UserRepositoryTest.JAVAJIGI;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    private DeleteHistory deleteHistory;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(JAVAJIGI);
        deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, javajigi, LocalDateTime.now());
    }

    @Nested
    @DisplayName("명령")
    class Command {
        @Test
        @DisplayName("새로운 삭제 내역을 추가한다.")
        void save() {
            DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getContentId()).isEqualTo(deleteHistory.getContentId()),
                    () -> assertThat(actual.getContentType()).isEqualTo(deleteHistory.getContentType()),
                    () -> assertThat(actual.getCreateDate()).isEqualTo(deleteHistory.getCreateDate()),
                    () -> assertThat(actual.deletedBy()).isEqualTo(deleteHistory.deletedBy())
            );
        }
    }
}
