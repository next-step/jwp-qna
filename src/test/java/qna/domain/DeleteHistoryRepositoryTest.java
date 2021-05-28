package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Test
	@DisplayName("DeleteHistory 저장")
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		DeleteHistory actual = deleteHistories.save(expected);
		assertThat(deleteHistories.findById(actual.getId()).isPresent()).isTrue();
	}

	@Test
	@DisplayName("DeleteHistory 조회")
	void findByID() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		DeleteHistory saved = deleteHistories.save(deleteHistory);
		assertThat(deleteHistories.findById(saved.getId()).isPresent()).isTrue();
		assertThat(deleteHistories.findById(saved.getId()).get()).isSameAs(saved);
	}

	@Test
	@DisplayName("DeleteHistory 삭제")
	void delete() {
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
		DeleteHistory saved = deleteHistories.save(deleteHistory);
		long savedId = saved.getId();
		deleteHistories.delete(saved);
		deleteHistories.flush();
		assertThat(deleteHistories.findById(savedId).isPresent()).isFalse();
	}

}
