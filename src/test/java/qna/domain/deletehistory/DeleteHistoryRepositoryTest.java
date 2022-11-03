package qna.domain.deletehistory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.deletehistory.DeleteHistoryTest.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("DeleteHistory Repository 테스트")
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Test
	@DisplayName("삭제 이력 저장 테스트")
	void saveTest() {
		DeleteHistory deleteHistory = deleteHistoryRepository.save(D1);
		assertThat(deleteHistory).isNotNull();
	}

	@Test
	@DisplayName("삭제 이력 저장 후 조회 테스트")
	void findByIdTest() {
		DeleteHistory deleteHistory1 = deleteHistoryRepository.save(D1);
		DeleteHistory deleteHistory2 = deleteHistoryRepository.save(D2);

		List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

		assertThat(deleteHistories).hasSize(2);
	}

	@Test
	@DisplayName("삭제 이력 저장 후 삭제 테스트")
	void deleteTest() {
		DeleteHistory deleteHistory1 = deleteHistoryRepository.save(D1);
		DeleteHistory deleteHistory2 = deleteHistoryRepository.save(D2);

		deleteHistoryRepository.delete(deleteHistory1);

		List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

		assertAll(
			() -> assertThat(deleteHistories).hasSize(1),
			() -> assertThat(deleteHistories).containsExactly(deleteHistory2)
		);
	}

}