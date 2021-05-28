package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository repository;

	private DeleteHistory answerDeleteHistory;

	@BeforeEach
	void setUp() {
		LocalDateTime now = LocalDateTime.now();
		long deletedById = 1L;
		long contentId = 1L;
		answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, contentId, deletedById, now);
	}

	@Test
	void saveAndFind() {
		DeleteHistory save = repository.save(answerDeleteHistory);
		List<DeleteHistory> contentTypes = repository.findDeleteHistoriesByContentType(
			ContentType.ANSWER);
		assertThat(contentTypes).hasSize(1);
		assertThat(contentTypes).contains(save);
	}
}
