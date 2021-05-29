package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
	@Autowired
	DeleteHistoryRepository deleteHistories;

	@Test
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, 2L, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertThat(actual).isEqualTo(expected);
	}
}