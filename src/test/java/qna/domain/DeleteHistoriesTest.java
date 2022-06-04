package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteHistoriesTest {
    private DeleteHistories deleteHistories;

    @BeforeEach
    void setUp() {
        //given
        deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, 1L, UserFixtures.JAVAJIGI, LocalDateTime.now()));
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, 2L, UserFixtures.SANJIGI, LocalDateTime.now()));
    }

    @Test
    @DisplayName("삭제기록 추가 후 사이즈 확인")
    void add() {
        //then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(2);
    }

    @Test
    @DisplayName("삭제기록 다건 추가 후 사이즈 확인")
    void addAll() {
        //given
        DeleteHistories addDeleteHistories = new DeleteHistories();
        addDeleteHistories.add(new DeleteHistory(ContentType.QUESTION, 1L, UserFixtures.JAVAJIGI, LocalDateTime.now()));
        addDeleteHistories.add(new DeleteHistory(ContentType.QUESTION, 2L, UserFixtures.SANJIGI, LocalDateTime.now()));

        //when
        deleteHistories.addAll(addDeleteHistories);

        //then
        assertThat(deleteHistories.getDeleteHistories()).hasSize(4);
    }
}
