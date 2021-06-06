package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteHistoriesTest {

    @Test
    void add() {
        //given
        DeleteHistories deleteHistories = new DeleteHistories();
        DeleteHistory deleteHistory = DeleteHistory.ofAnswer(1L, UserTest.JAVAJIGI, LocalDateTime.now());

        //when
        deleteHistories.add(deleteHistory);

        //then
        assertThat(deleteHistories.getDeleteHistories().get(0)).isSameAs(deleteHistory);
    }

    @Test
    void addAll() {
        //given
        DeleteHistories deleteHistories1 = new DeleteHistories();
        DeleteHistory ofAnswer = DeleteHistory.ofAnswer(1L, UserTest.JAVAJIGI, LocalDateTime.now());
        deleteHistories1.add(ofAnswer);

        DeleteHistories deleteHistories2 = new DeleteHistories();
        DeleteHistory ofQuestion = DeleteHistory.ofQuestion(1L, UserTest.JAVAJIGI, LocalDateTime.now());
        deleteHistories2.add(ofQuestion);

        //when
        deleteHistories1.addAll(deleteHistories2);

        //then
        assertThat(deleteHistories1.getDeleteHistories()).containsExactly(ofAnswer, ofQuestion);
    }

    @DisplayName("삭제기록 일급컬렉션에서 꺼낸 컬렉션에 직접 추가할 수 없다.")
    @Test
    void getDeleteHistoriesException() {
        //given
        DeleteHistories deleteHistories = new DeleteHistories();
        List<DeleteHistory> deleteHistoryList = deleteHistories.getDeleteHistories();

        //when
        assertThatThrownBy(() -> deleteHistoryList.add(DeleteHistory.ofAnswer(1L, UserTest.JAVAJIGI, LocalDateTime.now())))
                .isInstanceOf(UnsupportedOperationException.class); //then
    }
}
