package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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

    @Autowired
    private UserRepository users;

    private DeleteHistory savedDeleteHistory;
    private User savedUser;

    @BeforeEach
    void init() {
        savedUser = users.save(UserTest.JAVAJIGI);
        savedDeleteHistory = deleteHistories.save(new DeleteHistory(ContentType.ANSWER, 1L, savedUser, LocalDateTime
            .now()));
    }

    @Test
    @DisplayName("저장 테스트")
    void save() {
        // given & when & then
        assertAll(
            () -> assertThat(savedDeleteHistory.getId()).isNotNull(),
            () -> assertThat(savedDeleteHistory.getContentId()).isEqualTo(DeleteHistoryTest.D1.getContentId()),
            () -> assertThat(savedDeleteHistory.getContentType()).isEqualTo(DeleteHistoryTest.D1.getContentType()),
            () -> assertThat(savedDeleteHistory.getDeletedBy()).isEqualTo(savedUser)
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
