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
		Question actual = questionRepository.save(QuestionTest.Q2);
		Question expected = questionRepository.findById(QuestionTest.Q2.getId()).get();

		assertThat(actual).isEqualTo(expected);
		assertThat(actual).isSameAs(expected);
	}
}
