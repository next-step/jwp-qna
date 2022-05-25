package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
	@DisplayName("삭제 History 생성")
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, QuestionTest.Q1.getId(), UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistory actual = deleteHistoryRepository.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
		);
	}

	@Test
	@DisplayName("삭제 History 조회")
	void findAll() {
		DeleteHistory actual1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, QuestionTest.Q1.getId(), UserTest.JAVAJIGI, LocalDateTime.now()));
		DeleteHistory actual2 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, QuestionTest.Q1.getId(), UserTest.SANJIGI, LocalDateTime.now()));

		assertThat(deleteHistoryRepository.findAll()).contains(actual1, actual2);
	}
}
