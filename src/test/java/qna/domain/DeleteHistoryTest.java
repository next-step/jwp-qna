package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryTest {

	@Autowired
	DeleteHistoryRepository deleteHistoryRepository;

	@BeforeAll
	static void setUp(@Autowired UserRepository userRepository) {
		userRepository.save(UserTest.JAVAJIGI);
		userRepository.save(UserTest.SANJIGI);
	}

	@Test
	void 삭제_이력_저장테스트() {
		// given
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, 1L, UserTest.SANJIGI);

		// when
		DeleteHistory expectDeleteHistory = deleteHistoryRepository.save(deleteHistory);

		// then
		assertAll(
			() -> assertThat(expectDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION)
		);
	}

	@Test
	void 삭제_이력_조회테스트() {
		// given
		List<DeleteHistory> deleteHistory = Arrays.asList(
			new DeleteHistory(ContentType.QUESTION, 1L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.ANSWER, 3L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.QUESTION, 4L, UserTest.SANJIGI)
		);
		deleteHistoryRepository.saveAll(deleteHistory);

		// when
		List<DeleteHistory> expectDeleteHistory = deleteHistoryRepository.findAll();

		// then
		assertAll(
			() -> assertThat(expectDeleteHistory).hasSize(deleteHistory.size()),
			() -> assertThat(expectDeleteHistory).containsAll(deleteHistory)
		);
	}

	@Test
	void 삭제_이력_삭제테스트() {
		// given
		List<DeleteHistory> deleteHistory = Arrays.asList(
			new DeleteHistory(ContentType.QUESTION, 1L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.ANSWER, 3L, UserTest.SANJIGI),
			new DeleteHistory(ContentType.QUESTION, 4L, UserTest.SANJIGI)
		);
		List<DeleteHistory> expectDeleteHistory = deleteHistoryRepository.saveAll(deleteHistory);

		// when
		deleteHistoryRepository.deleteAll();

		List<DeleteHistory> deletedHistoryList = deleteHistoryRepository.findAll();

		// then
		assertAll(
			() -> assertThat(expectDeleteHistory).hasSize(deleteHistory.size()),
			() -> assertThat(deletedHistoryList).hasSize(0)
		);
	}
}