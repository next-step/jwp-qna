package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("DeleteHistory를 저장할 수 있다.")
    @Test
    void save_and_find() {
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.D1);

        Optional<DeleteHistory> expect = deleteHistoryRepository.findById(actual.getId());

        assertThat(expect).isPresent();
    }


    @DisplayName("저장된 DeleteHistory를 삭제할 수 있다.")
    @Test
    void delete() {
        DeleteHistory actual = deleteHistoryRepository.save(DeleteHistoryTest.D1);

        deleteHistoryRepository.delete(actual);
        Optional<DeleteHistory> byId = deleteHistoryRepository.findById(actual.getId());

        assertThat(byId).isNotPresent();
    }

}
