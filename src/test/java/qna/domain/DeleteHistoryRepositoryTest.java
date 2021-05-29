package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistories;

    private DeleteHistory savedDeleteHistory;

    @BeforeEach
    void init() {
        savedDeleteHistory = deleteHistories.save(DeleteHistoryTest.D1);
    }

    @Test
    @DisplayName("저장 테스트")
    void save() {
        // given & when & then
        assertAll(
            () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
            () -> assertThat(savedDeleteHistory.getContentId()).isEqualTo(DeleteHistoryTest.D1.getContentId()),
            () -> assertThat(savedDeleteHistory.getContentType()).isEqualTo(DeleteHistoryTest.D1.getContentType()),
            () -> assertThat(savedDeleteHistory.getDeletedBy()).isEqualTo(DeleteHistoryTest.D1.getDeletedBy())
        );
    }

    @Test
    @DisplayName("영속성 동일성 테스트")
    void findById() {
        // given & when
        DeleteHistory actual = deleteHistories.findById(savedDeleteHistory.getId()).get();
        // then
        assertThat(actual).isEqualTo(savedDeleteHistory);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        // given & when
        deleteHistories.delete(savedDeleteHistory);
        // then
        assertThat(deleteHistories.findById(savedDeleteHistory.getId())).isNotPresent();
    }
}
