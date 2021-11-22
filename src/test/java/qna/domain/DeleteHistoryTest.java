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
		Question question = new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents());
		questionRepository.save(question);
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), 0L, LocalDateTime.now());

		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);
		assertThat(actual).isEqualTo(deleteHistory);
		assertThat(actual).isSameAs(deleteHistory);
	}

	@Test
	void findById() {
		Question question = new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents());
		Question actual = questionRepository.save(question);
		Question expected = questionRepository.findById(question.getId()).get();

		assertThat(actual).isEqualTo(expected);
		assertThat(actual).isSameAs(expected);
	}
}
