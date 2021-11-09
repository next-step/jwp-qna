package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(QUESTION, 1L, 1L, LocalDateTime.of(2021, 11, 8, 0, 0, 0));
    public static final DeleteHistory DH2 = new DeleteHistory(ANSWER, 5L, 2L, LocalDateTime.of(2021, 11, 9, 0, 0, 0));

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private static Stream<Arguments> provideDeleteHistories() {
        return Stream.of(
            Arguments.of(DH1),
            Arguments.of(DH2)
        );
    }

    @DisplayName("DeleteHistory객체룰 입력으로 받는 save통하여 저장한 후 조회하면, 결과의 속성과 입력객체의 속성은 동일하다.")
    @ParameterizedTest
    @MethodSource("provideDeleteHistories")
    void saveTest(DeleteHistory expected) {
        DeleteHistory actual = deleteHistoryRepository.save(expected);
        assertThat(actual).isEqualTo(expected);
    }
}
