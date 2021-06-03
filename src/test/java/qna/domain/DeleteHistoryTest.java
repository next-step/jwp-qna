package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class DeleteHistoryTest {
	public static final DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 1L, 2L);

	private final DeleteHistoryRepository deleteHistoryRepository;

	@Autowired
	DeleteHistoryTest(DeleteHistoryRepository deleteHistoryRepository) {
		this.deleteHistoryRepository = deleteHistoryRepository;
	}

	@Test
	void save_test() {
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByContentType_test() {
		deleteHistoryRepository.save(deleteHistory);
		DeleteHistory actual = deleteHistoryRepository.findFirstByContentType(ContentType.ANSWER).get();
		assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER);
	}
}