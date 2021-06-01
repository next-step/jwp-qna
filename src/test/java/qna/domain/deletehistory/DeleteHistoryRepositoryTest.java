package qna.domain.deletehistory;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository repository;

	private DeleteHistory answerDeleteHistory;

	@Autowired
	private UserRepository userRepository;
	@BeforeEach
	void setUp() {
		LocalDateTime now = LocalDateTime.now();
		User deletedBy = userRepository.save(UserTest.JAVAJIGI);;
		long contentId = 1L;
		answerDeleteHistory = new DeleteHistory(ContentType.ANSWER, new ContentId(contentId), deletedBy, now);
	}

	@Test
	void saveAndFind() {
		DeleteHistory save = repository.save(answerDeleteHistory);
		List<DeleteHistory> contentTypes = repository.findDeleteHistoriesByContentType(
			ContentType.ANSWER);
		Assertions.assertThat(contentTypes).hasSize(1);
		Assertions.assertThat(contentTypes).contains(save);
	}
}
