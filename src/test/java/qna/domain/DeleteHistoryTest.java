package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("delete_history 엔티티 테스트")
@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory DELETE_QUESTION_HISTORY = new DeleteHistory(ContentType.QUESTION, 1L, 1L);
    public static final DeleteHistory DELETE_ANSWER_HISTORY = new DeleteHistory(ContentType.ANSWER, 2L, 1L);

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @DisplayName("save 성공")
    @Test
    void save_question_success() {
        assertThatNoException().isThrownBy(() -> deleteHistoryRepository.save(DELETE_QUESTION_HISTORY));
    }
}
