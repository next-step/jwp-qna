package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("DeleteHistory를 저장한다.")
    void save() {
        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L);

        // when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);

        // then
        assertAll(
                () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
                () -> assertThat(savedDeleteHistory.getCreateDate()).isNotNull(),
                () -> assertThat(savedDeleteHistory).isEqualTo(deleteHistory)
        );
    }
}
