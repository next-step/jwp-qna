package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.ContentType.*;
import static qna.domain.UserTest.*;

import java.time.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;

@DataJpaTest
public class DeleteHistoryTest {
    public static final DeleteHistory DH1 = new DeleteHistory(QUESTION, 1L, JAVAJIGI,
        LocalDateTime.of(2021, 11, 8, 0, 0, 0));
    public static final DeleteHistory DH2 = new DeleteHistory(ANSWER, 5L, SANJIGI,
        LocalDateTime.of(2021, 11, 9, 0, 0, 0));

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
