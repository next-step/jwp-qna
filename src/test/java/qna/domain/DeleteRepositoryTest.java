package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.UserTest.JAVAJIGI;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteRepositoryTest {
    private static final DeleteHistory deleteHistoryTest = new DeleteHistory(QUESTION, 1L, JAVAJIGI, LocalDateTime.now());

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void init() {
        deleteHistoryRepository.save(deleteHistoryTest);
    }

    @Test
    void findById() {
        DeleteHistory deleteHistory = deleteHistoryRepository.findById(1L).get();
        assertAll(
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(QUESTION),
                () -> assertThat(deleteHistory.getContentId()).isEqualTo(1L),
                () -> assertThat(deleteHistory.getDeletedBy()).isEqualTo(1L)
        );
    }
}
