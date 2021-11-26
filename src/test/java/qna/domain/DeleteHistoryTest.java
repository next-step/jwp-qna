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
	@Autowired
	private UserRepository userRepository;

	@Test
	void save() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question question = questionRepository.save(QuestionTest.Q1.writeBy(user));
		DeleteHistory actual = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.QUESTION, question.getId(),
				question.getWriter().getId(),
				LocalDateTime.now()));

		assertThat(actual).isNotNull();
	}

	@Test
	void findById() {
		User user = userRepository.save(UserTest.SANJIGI);
		questionRepository.save(QuestionTest.Q2.writeBy(user));
		DeleteHistory expected = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.QUESTION, QuestionTest.Q2.getId(),
				QuestionTest.Q2.getWriter().getId(),
				LocalDateTime.now()));

		Optional<DeleteHistory> actual = deleteHistoryRepository.findById(1L);

		assertThat(actual).hasValue(expected);
	}
}
