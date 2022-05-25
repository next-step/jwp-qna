package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @BeforeEach
    void before() {
        deleteHistoryRepository.deleteAll();
    }

    @DisplayName("DeleteHistory 저장 테스트")
    @Test
    void saveTest() {
        DeleteHistory expected = DeleteHistoryTest.DH1;
        DeleteHistory result = deleteHistoryRepository.save(expected);

        assertThat(result).isNotNull();
        assertThat(result.getContentType()).isEqualTo(expected.getContentType());
    }

    @DisplayName("DeleteHistory 조회 테스트")
    @Test
    void findTest() {
        DeleteHistory expected = deleteHistoryRepository.save(DeleteHistoryTest.DH1);
        Optional<DeleteHistory> resultOptional = deleteHistoryRepository.findById(expected.getId());
        assertThat(resultOptional).isNotEmpty();

        DeleteHistory result = resultOptional.get();
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);
    }
}