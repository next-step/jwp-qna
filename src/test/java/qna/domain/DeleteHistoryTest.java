package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

	private User user;
	private Question question;
	private DeleteHistory deleteHistory;

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		question = questionRepository.save(new Question("questionTitle", "questionContents").writeBy(user));
		deleteHistory = deleteHistoryRepository.save(
			new DeleteHistory(ContentType.QUESTION, question.getId(),
				question.getWriter()));
	}

	@Test
	void save() {
		assertThat(deleteHistory).isNotNull();
	}

	@Test
	void findById() {
		Optional<DeleteHistory> actual = deleteHistoryRepository.findById(deleteHistory.getId());

		assertThat(actual).hasValue(deleteHistory);
	}

	@Test
	void createWithDeletedBy() {
		assertThat(deleteHistory.getDeletedBy()).isEqualTo(user);
	}

	@Test
	void auditCreatedDate() {
		assertThat(deleteHistory.getCreateDate()).isNotNull();
	}
}
