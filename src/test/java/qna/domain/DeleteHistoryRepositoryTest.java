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
	UserRepository users;

	@Autowired
	DeleteHistoryRepository deleteHistories;

	User savedWriter;

	DeleteHistory savedDeleteHistory;

	@BeforeEach
	void setUp() {
		User user = new User("wrallee", "pasword", "우찬", "wrallee@naver.com");
		savedWriter = users.save(user);
		savedDeleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, savedWriter, LocalDateTime.now());
	}

	@Test
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, savedWriter, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertSame(expected, actual);
	}
}
