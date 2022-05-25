package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DeleteHistoryRepositoryTest {

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("DeleteHistory 저장")
    @Test
    void save() {
        final DeleteHistory expected = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, null));

        final Optional<DeleteHistory> actual = deleteHistoryRepository.findById(1L);

        assertThat(actual).isPresent();
        assertThat(actual.get()).isSameAs(expected);
    }

}