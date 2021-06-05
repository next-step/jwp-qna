package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class DeleteHistoryTest {
	private final DeleteHistoryRepository deleteHistoryRepository;
	private final UserRepository userRepository;

	@Autowired
	DeleteHistoryTest(DeleteHistoryRepository deleteHistoryRepository, UserRepository userRepository) {
		this.deleteHistoryRepository = deleteHistoryRepository;
		this.userRepository = userRepository;
	}

	@BeforeEach
	void setUp() {
		User user = userRepository.save(JAVAJIGI);
		deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 1L, user));
	}

	@Test
	void save_test() {
		DeleteHistory actual = deleteHistoryRepository.findAll().get(0);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByContentType_test() {
		DeleteHistory actual = deleteHistoryRepository.findFirstByContentType(ContentType.ANSWER).get();
		assertThat(actual.getContentType()).isEqualTo(ContentType.ANSWER);
	}

	@Test
	void getUser_test() {
		DeleteHistory deleteHistory = deleteHistoryRepository.findAll().get(0);
		User actual = deleteHistory.getDeletedBy();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
	}
}
