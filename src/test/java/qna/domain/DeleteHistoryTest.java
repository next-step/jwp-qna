package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;
	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void save() {
		questionRepository.save(QuestionTest.Q1);
		DeleteHistory actual = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(),
				UserTest.JAVAJIGI.getId(),
				LocalDateTime.now()));

		assertThat(actual).isNotNull();
	}

	@Test
	void findById() {
		questionRepository.save(QuestionTest.Q2);
		DeleteHistory expected = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.QUESTION, QuestionTest.Q2.getId(),
				QuestionTest.Q2.getWriter().getId(),
				LocalDateTime.now()));

		Optional<DeleteHistory> actual = deleteHistoryRepository.findById(1L);

		assertThat(actual).hasValue(expected);
	}
}
