package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;

import java.time.LocalDateTime;
import java.time.Month;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("DeleteHistory 데이터 save 하는 테스트 진행")
    @Test
    public void save() {

        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.of(2021, Month.AUGUST, 1, 10, 10));

        // when
        DeleteHistory result = deleteHistoryRepository.save(deleteHistory);

        // then
        Assertions.assertThat(result).isEqualTo(deleteHistory);
    }

    @DisplayName("DeleteHistory 데이터 find 하는 테스트 진행")
    @Test
    public void find() {

        // given
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.of(2021, Month.AUGUST, 1, 10, 10));
        DeleteHistory result = deleteHistoryRepository.save(deleteHistory);

        // when
        DeleteHistory find = deleteHistoryRepository.findById(result.getId()).get();

        // then
        Assertions.assertThat(find).isEqualTo(result);

    }

}
