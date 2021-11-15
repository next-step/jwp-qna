package qna.domain;

import static qna.domain.DeleteHistoryTest.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	void 엔티티맵핑_확인() {
		// given
		User javajigi = userRepository.save(UserTest.JAVAJIGI);
		Question question = new Question("how to write name", "free");
		questionRepository.save(question);

		// when
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question, javajigi, LocalDateTime.now());
		DeleteHistory expected = deleteHistoryRepository.save(deleteHistory);

		Assertions.assertThat(expected.equals(deleteHistory)).isTrue();
	}
}
