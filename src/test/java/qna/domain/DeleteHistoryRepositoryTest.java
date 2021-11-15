package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
	@Autowired
	DeleteHistoryRepository deleteHistoryRepository;

	@Test
	@DisplayName("DeleteHistory 생성 테스트")
	public void DeleteHistoryRepositoryCreateTest() {
		//given
		//when
		DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));

		//then
		DeleteHistory deleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
		assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
	}

	@Test
	@DisplayName("DeleteHistory 동일성 테스트")
	public void DeleteHistoryRepositoryEqualsTest() {
		//given
		//when
		DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
		DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();

		//then
		assertThat(savedDeleteHistory).isEqualTo(findDeleteHistory);
		assertThat(savedDeleteHistory).isSameAs(findDeleteHistory);
	}

	@Test
	@DisplayName("DeleteHistory 변경 테스트")
	public void DeleteHistoryRepositoryUpdateTest() {
		//given
		DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
		//when
		savedDeleteHistory.setContentType(ContentType.QUESTION);
		deleteHistoryRepository.flush();

		//then
		DeleteHistory findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId()).get();
		assertThat(findDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
	}

	@Test
	@DisplayName("DeleteHistory 삭제 테스트")
	public void DeleteHistoryRepositoryDeleteTest() {
		//given
		DeleteHistory savedOne = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));
		DeleteHistory savedTwo = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, 1L, LocalDateTime.now()));

		//when
		deleteHistoryRepository.deleteById(savedOne.getId());

		//then
		assertThat(deleteHistoryRepository.findAll()).hasSize(1);
		assertThat(deleteHistoryRepository.findAll()).contains(savedTwo);
	}
}