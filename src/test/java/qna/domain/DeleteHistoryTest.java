package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

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
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(),
			UserTest.JAVAJIGI.getId(),
			LocalDateTime.now());
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

		assertThat(actual).isNotNull();
	}

	@Test
	void findById() {
		questionRepository.save(QuestionTest.Q2);
		DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, QuestionTest.Q2.getId(),
			QuestionTest.Q2.getWriterId(),
			LocalDateTime.now());
		deleteHistoryRepository.save(expected);

		DeleteHistory actual = deleteHistoryRepository.findById(1L).get();
		assertThat(actual).isEqualTo(expected);
		assertThat(actual).isSameAs(expected);
	}
}
