package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoryListTest {

    @Test
    @DisplayName("삭제히스토리 리스트 생성 및 한건 추가 성공")
    public void addDeleteHistoryList() {
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI);

        DeleteHistoryList deleteHistoryList = new DeleteHistoryList(deleteHistory);

        assertThat(deleteHistoryList.size()).isEqualTo(1);
    }
}