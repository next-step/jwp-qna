package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("DeleteHistory 테스트")
class DeleteHistoryTest {
    public static final DeleteHistory H1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L);
    public static final DeleteHistory H2 = new DeleteHistory(ContentType.QUESTION, 2L, 2L);

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    private static Stream<Arguments> testQuestions() {
        return Stream.of(Arguments.of(H1), Arguments.of(H2));
    }

    @DisplayName("Save 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @MethodSource("testQuestions")
    void save_확인(DeleteHistory expectedResult) {
        DeleteHistory result = deleteHistoryRepository.save(expectedResult);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectedResult.getId()),
                () -> assertThat(result.getContentType()).isEqualTo(expectedResult.getContentType()),
                () -> assertThat(result.getContentId()).isEqualTo(expectedResult.getContentId()),
                () -> assertThat(result.getDeletedById()).isEqualTo(expectedResult.getDeletedById()),
                () -> assertThat(result.getCreateDate()).isEqualTo(expectedResult.getCreateDate())
        );
    }
}