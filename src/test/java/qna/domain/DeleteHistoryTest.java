package qna.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory D1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
    public static final DeleteHistory D2 = new DeleteHistory(ContentType.QUESTION, 2L, UserTest.SANJIGI, LocalDateTime.now());

    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    private static Stream<Arguments> providerDeleteHistories() {
        return Stream.of(
                Arguments.of(D1),
                Arguments.of(D2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerDeleteHistories")
    public void 삭제_이력_생성(DeleteHistory excepted) {
        DeleteHistory actual = deleteHistoryRepository.save(excepted);

        assertThat(actual).isEqualTo(excepted);
    }

}