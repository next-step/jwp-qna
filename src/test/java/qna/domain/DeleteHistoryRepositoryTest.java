package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.UserRepositoryTest.user;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 삭제_히스토리를_저장한다() {
        User user = userRepository.save(user());
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, user);
        DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);
        assertAll(
                () -> assertThat(expected.getId()).isEqualTo(1L),
                () -> assertThat(expected.getContentId()).isEqualTo(1L),
                () -> assertThat(expected.getDeletedBy()).isEqualTo(user),
                () -> assertThat(expected.getContentType()).isEqualTo(ContentType.QUESTION),
                () -> assertThat(expected.getCreateDate()).isBefore(LocalDateTime.now())
        );
    }
}