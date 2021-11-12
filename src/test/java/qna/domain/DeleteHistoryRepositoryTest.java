package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    DeleteHistory ANSWER_HISTORY = new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now());
    DeleteHistory QUESTION_HISTORY = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistories;

    @Test
    @DisplayName("DeleteHistory 저장 후 ID not null 체크")
    void save() {
        // when
        DeleteHistory expect = deleteHistories.save(ANSWER_HISTORY);

        // then
        assertThat(expect.getId()).isNotNull();
    }

    @Test
    @DisplayName("DeleteHistory 저장 후 DB조회 객체 동일성 체크")
    void identity() {
        // when
        DeleteHistory actual = deleteHistories.save(QUESTION_HISTORY);
        DeleteHistory expect = deleteHistories.findById(actual.getId()).get();

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
