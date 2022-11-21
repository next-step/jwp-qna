package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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

    @DisplayName("질문 삭제이력을 저장할 수 있다.")
    @Test
    void saveQuestionHistory() {
        final User user = new User(1L, "dominiqn", "password", "Me", "dmut7691@gmail.com");
        final Question question = new Question(2L, "질문입니다", "별 내용 없습니다.").writeBy(user);
        final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 2L, 1L);

        final DeleteHistory actual = DeleteHistory.of(question);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("질문은 null이 아니어야 한다.")
    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @NullSource
    void nullQuestion(Question question) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DeleteHistory.of(question));
    }
}
