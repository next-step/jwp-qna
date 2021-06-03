package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.ContentType.ANSWER;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

class DeleteHistoriesTest {

    private DeleteHistory questionDelete;
    private DeleteHistories answerDeletes;
    private DeleteHistories deleteHistories;

    @Test
    @DisplayName("deleteHistoriy 들을 합친 컬렉션을 리턴한다.")
    void merge() {
        questionDelete = new DeleteHistory(QUESTION, 1L, JAVAJIGI, now());
        answerDeletes = new DeleteHistories(new DeleteHistory(ANSWER, 1L, SANJIGI, now()));

        deleteHistories = new DeleteHistories(questionDelete);

        List<DeleteHistory> mergedResults = deleteHistories.merge(answerDeletes);

        assertAll(
                () -> assertThat(mergedResults).hasSize(2),
                () -> assertThat(mergedResults.get(0).getContentType()).isEqualTo(QUESTION),
                () -> assertThat(mergedResults.get(0).getDeleteUser()).isEqualTo(JAVAJIGI),
                () -> assertThat(mergedResults.get(1).getContentType()).isEqualTo(ANSWER),
                () -> assertThat(mergedResults.get(1).getDeleteUser()).isEqualTo(SANJIGI)
        );
    }
}
