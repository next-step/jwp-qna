package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("삭제 히스토리에 목록 대한 단위 테스트")
class DeleteHistoriesTest {

    @DisplayName("삭제 히스토리 객체에 단건을 넣어 생성하면 정상적으로 생성된다")
    @Test
    void deleteHistories_test() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(
            new Question(1L, "test-title", "test-contents"));

        DeleteHistories deleteHistories = new DeleteHistories(deleteHistory);
        assertThat(deleteHistories.getItems()).hasSize(1);
    }

    @DisplayName("삭제 히스토리 객체에 컬렉션을 넣어 생성하면 정상적으로 생성된다")
    @Test
    void deleteHistories_test2() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(
            new Question(1L, "test-title", "test-contents"));
        DeleteHistory deleteHistory2 = DeleteHistory.ofQuestion(
            new Question(2L, "test-title", "test-contents"));

        DeleteHistories deleteHistories = new DeleteHistories(Arrays.asList(deleteHistory, deleteHistory2));
        assertThat(deleteHistories.getItems()).hasSize(2);
    }

    @DisplayName("삭제 히스토리 객체에 단건, 컬렉션을 넣어 생성한 객체를 머지하면 정상적으로 머지된다")
    @Test
    void deleteHistories_test3() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(
            new Question(1L, "test-title", "test-contents"));
        DeleteHistory deleteHistory2 = DeleteHistory.ofQuestion(
            new Question(2L, "test-title", "test-contents"));
        DeleteHistory deleteHistory3 = DeleteHistory.ofQuestion(
            new Question(3L, "test-title", "test-contents"));

        DeleteHistories items = new DeleteHistories(deleteHistory);
        DeleteHistories items2 = new DeleteHistories(Arrays.asList(deleteHistory2, deleteHistory3));

        DeleteHistories result = DeleteHistories.merge(items, items2);
        assertThat(result.getItems()).hasSize(3);
    }

    @DisplayName("가져온 삭제 히스토리를 수정하려 하면 예외가 발생한다")
    @Test
    void deleteHistories_exception() {
        DeleteHistory deleteHistory = DeleteHistory.ofQuestion(
            new Question(1L, "test-title", "test-contents"));

        DeleteHistories deleteHistories = new DeleteHistories(deleteHistory);
        List<DeleteHistory> items = deleteHistories.getItems();

        assertThatThrownBy(() -> {
            items.add(DeleteHistory.ofQuestion(new Question(2L, "test-title", "test-contents")));
        }).isInstanceOf(UnsupportedOperationException.class);
    }
}
