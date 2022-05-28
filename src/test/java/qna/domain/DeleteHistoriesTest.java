package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class DeleteHistoriesTest {

    private List<DeleteHistory> deleteHistoryList;

    @BeforeEach
    void setUp() {
        deleteHistoryList = Collections.singletonList(new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now()));
    }

    @DisplayName("삭제된 History 정보들를 입력 받는다.")
    @Test
    void createWhenInputIsArray() {
        assertThat(new DeleteHistories(deleteHistoryList)).isEqualTo(new DeleteHistories(deleteHistoryList));
    }

    @DisplayName("잘못된 입력값 입력시 에러를 발생한다.")
    @Test
    void invalid() {
        assertThatThrownBy(() -> new DeleteHistories(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("동일한 삭제 History 정보를 가질수 없다.")
    @Test
    void noDuplicateDeleteHistory() {
        List<DeleteHistory> deleteHistoryList = Arrays.asList(
                new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now())
        );
        DeleteHistories deleteHistories = new DeleteHistories(deleteHistoryList);
        assertThat(deleteHistories.size()).isEqualTo(1);
    }

    @DisplayName("삭제 History 정보를 추가할수 있다.")
    @Test
    void add() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI, LocalDateTime.now());
        DeleteHistories deleteHistories = new DeleteHistories(deleteHistoryList);
        deleteHistories.add(deleteHistory);
        deleteHistories.add(deleteHistory);
        assertThat(deleteHistories.size()).isEqualTo(2);
    }
}
