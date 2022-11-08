package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

public class DeleteHistoriesTest {

    @Test
    @DisplayName("DeleteHistories 객체 생성")
    void create_deleteHistories() {
        DeleteHistory deleteHistory = DeleteHistory.create(ContentType.QUESTION, QuestionTest.Q1, UserTest.JAVAJIGI);
        assertThatCode(() -> new DeleteHistories(Collections.singletonList(deleteHistory))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("서로 다른 DeleteHistories 머지")
    void merge_delete_histories() {
        DeleteHistory deleteHistory = DeleteHistory.create(ContentType.QUESTION, QuestionTest.Q1, UserTest.JAVAJIGI);
        DeleteHistories deleteHistories = new DeleteHistories(Collections.singletonList(deleteHistory));

        DeleteHistory deleteHistoryTwo = DeleteHistory.create(ContentType.QUESTION, QuestionTest.Q2, UserTest.JAVAJIGI);
        DeleteHistories deleteHistoriesTwo = new DeleteHistories(Collections.singletonList(deleteHistoryTwo));

        assertThat(DeleteHistories.merge(deleteHistories, deleteHistoriesTwo).deleteHistoryCount()).isEqualTo(2);
    }
}
