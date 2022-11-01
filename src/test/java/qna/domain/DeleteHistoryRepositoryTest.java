package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeleteHistoryRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private static final DeleteHistory questionDeleted =
            new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
    private static final DeleteHistory answerDeleted =
            new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());


    @Test
    void deleteHistory_저장_테스트() {
        DeleteHistory actual = deleteHistoryRepository.save(questionDeleted);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(ContentType.QUESTION, actual.getContentType()),
                () -> assertEquals(1L, actual.getContentId()),
                () -> assertEquals(1L, actual.getDeletedById()),
                () -> assertThat(actual.getCreateDate()).isNotNull()
        );
    }

    @Test
    void deleteHistory_동일성_보장_테스트() {
        DeleteHistory deleteHistorySaved = deleteHistoryRepository.save(questionDeleted);
        DeleteHistory deleteHistoryFound =
                deleteHistoryRepository.findById(deleteHistorySaved.getId()).get();

        assertTrue(deleteHistorySaved == deleteHistoryFound);
    }

    @Test
    void answer_삭제_테스트() {
        DeleteHistory deleteHistory = deleteHistoryRepository.save(answerDeleted);

        deleteHistoryRepository.deleteById(deleteHistory.getId());

        assertThat(deleteHistoryRepository.findById(deleteHistory.getId())).isEmpty();
    }

}
