package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import qna.config.JpaAuditingConfiguration;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(value = {JpaAuditingConfiguration.class})
class DeleteHistoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    @DisplayName("저장")
    void save() {
        //given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());

        //expect
        assertThat(deleteHistoryRepository.save(deleteHistory)).isNotNull();
    }
}