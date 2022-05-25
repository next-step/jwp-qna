package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DeleteHistoriesTest {
    private static DeleteHistory deleteQuestion = DeleteHistory.ofQuestion(1L, UserTest.JAVAJIGI);
    private static DeleteHistory deleteAnswer1 = DeleteHistory.ofAnswer(2L, UserTest.JAVAJIGI);
    private static DeleteHistory deleteAnswer2 = DeleteHistory.ofAnswer(3L, UserTest.JAVAJIGI);


    @DisplayName("DeleteHistories 일급 컬렉션 생성")
    @Test
    void test_new() {
        //given & when
        DeleteHistories deleteHistories = DeleteHistories.empty();
        //then
        assertThat(deleteHistories).isNotNull();
    }

    @DisplayName("DeleteHistories 일급 컬렉션에 DeleteHistory 추가")
    @Test
    void test_add_delete_history() {
        //given
        DeleteHistories deleteHistories = DeleteHistories.empty();
        //when
        deleteHistories.addDeleteHistory(deleteQuestion);
        deleteHistories.addDeleteHistory(deleteAnswer1);
        //then
        assertAll(
                () -> assertThat(deleteHistories.getDeleteHistories()).hasSize(2),
                () -> assertThat(deleteHistories.getDeleteHistories()).contains(deleteQuestion, deleteAnswer1)
        );
    }

    @DisplayName("DeleteHistories 일급 컬렉션에 DeleteHistories 추가")
    @Test
    void test_add_all() {
        //given
        List<DeleteHistory> deleteHistories = new ArrayList<>(Arrays.asList(deleteQuestion, deleteAnswer1));
        DeleteHistories newDeleteHistories = DeleteHistories.from(deleteHistories);
        DeleteHistories addDeleteHistories = DeleteHistories.from(Collections.singletonList(deleteAnswer2));
        //when
        newDeleteHistories.addAll(addDeleteHistories);
        //then
        assertAll(
                () -> assertThat(newDeleteHistories.getDeleteHistories()).hasSize(3),
                () -> assertThat(newDeleteHistories.getDeleteHistories()).contains(deleteAnswer2)
        );
    }
}