package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository users;

    @DisplayName("DeleteHistory가 저장된다")
    @Test
    void testSave() {
        User writer = users.save(new User("userId", "password", "name", "email"));
        DeleteHistory deleteHistory = DeleteHistory.of(1L, writer);
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getContentId()).isEqualTo(deleteHistory.getContentId()),
                () -> assertThat(savedDeleteHistory.getDeletedBy()).isEqualTo(deleteHistory.getDeletedBy())
        );
    }
}
