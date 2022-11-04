package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class DeleteHistoryTest {

	@Test
	void 동등성() {
		DeleteHistory deleteHistory1 = new DeleteHistory(1L, ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		DeleteHistory deleteHistory2 = new DeleteHistory(2L, ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		assertThat(deleteHistory1).isNotEqualTo(deleteHistory2);
		assertThat(deleteHistory1).isEqualTo(new DeleteHistory(1L, ContentType.QUESTION, 1L, 1L, LocalDateTime.now()));
	}
}
