package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("삭제이력 엔티티")
public class DeleteHistoryTest extends JpaSliceTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("삭제이력을 저장할 수 있다.")
    @Test
    void save() {
        final DeleteHistory history1 = new DeleteHistory(ContentType.QUESTION, 1L, 2L);
        final DeleteHistory history2 = new DeleteHistory(ContentType.ANSWER, 3L, 4L);
        final List<DeleteHistory> histories = Arrays.asList(history1, history2);

        assertAll(
                () -> assertThatNoException().isThrownBy(() -> deleteHistoryRepository.saveAll(histories)),
                () -> assertThat(deleteHistoryRepository.count()).isEqualTo(2)
        );
    }
}
