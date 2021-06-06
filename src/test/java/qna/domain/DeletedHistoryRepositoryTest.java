package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.DELETED_ANSWER1;
import static qna.domain.DeleteHistoryTest.DELETE_HISTORY_ANSWER1;

@DataJpaTest
@DisplayName("DeletedHistoryRepository 테스트")
class DeletedHistoryRepositoryTest {

    @Autowired
    private AnswerRepository answers;

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    private Answer deletedAnswer;
    private DeleteHistory deleteHistoryAnswer;

    @BeforeEach
    void setUp() {
        deletedAnswer = answers.save(DELETED_ANSWER1);
        deleteHistoryAnswer = deleteHistories.save(DELETE_HISTORY_ANSWER1);
    }

    @Test
    @DisplayName("findById_정상_저장_전_후_동일한_객체_조회")
    void findById_정상_저장_전_후_동일한_객체_조회() {
        // Given
        DeleteHistory expectedResult = deleteHistoryAnswer;

        // When
        Optional<DeleteHistory> actualResult = deleteHistories.findById(expectedResult.getId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }
}
