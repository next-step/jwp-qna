package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistoryTest {

    @Test
    @DisplayName("삭제 내역 생성")
    void create() {
        //given
        User writer = new User(null, "sangjae", "password", "name", "javajigi@slipp.net");
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, writer.getId(), writer, LocalDateTime.now());

        //expect
        assertThat(deleteHistory).isNotNull();
    }
}