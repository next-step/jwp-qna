package qna.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.UserTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
	@Autowired
	DeleteHistoryRepository deleteHistories;

	@Test
	@DisplayName("저장")
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertThat(actual).isEqualTo(expected);
	}
}