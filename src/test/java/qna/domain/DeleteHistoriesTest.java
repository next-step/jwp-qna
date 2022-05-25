package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeleteHistoriesTest {

    private DeleteHistories deleteHistories;

    @BeforeEach
    void setUp() {
        deleteHistories = new DeleteHistories();
        deleteHistories.add(DeleteHistory.ofQuestion(1L, UserTest.JAVAJIGI, LocalDateTime.now()));
    }

    @DisplayName("DeleteHistories에 DeleteHistory를 추가한다.")
    @Test
    void add() {
        //given & when
        deleteHistories.add(DeleteHistory.ofAnswer(1L, UserTest.JAVAJIGI, LocalDateTime.now()));

        //then
        assertThat(deleteHistories.elements()).hasSize(2);
    }

    @DisplayName("DeleteHistories에 DeleteHistories를 추가한다.")
    @Test
    void addAll() {
        //given
        DeleteHistories addDeleteHistories = new DeleteHistories(Arrays.asList(
                DeleteHistory.ofAnswer(2L, UserTest.JAVAJIGI, LocalDateTime.now()),
                DeleteHistory.ofAnswer(3L, UserTest.SANJIGI, LocalDateTime.now())
        ));

        //when
        deleteHistories.addAll(addDeleteHistories);

        //then
        assertThat(deleteHistories.elements()).hasSize(3);
    }

}
