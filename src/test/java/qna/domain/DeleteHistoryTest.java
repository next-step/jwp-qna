package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {
    @DisplayName("Answer 도메인 생성")
    @Test
    void test_new() {
        //given & when
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
        //then
        assertThat(deleteHistory).isNotNull();
    }
}