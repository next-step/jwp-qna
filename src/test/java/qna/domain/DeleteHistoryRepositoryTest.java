package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	DeleteHistoryRepository deleteHistories;

	DeleteHistory savedDeleteHistory;

	@BeforeEach
	void setUp() {
		savedDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
	}

	@Test
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertSame(expected, actual);
	}
}
