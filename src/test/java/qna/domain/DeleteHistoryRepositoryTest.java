package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	private DeleteHistory deleteHistory;

	@BeforeEach
	void setup() {
		User user = new User("testUser", "testPassword", "testName", "testEmail");
		User savedUser = this.userRepository.save(user);
		this.deleteHistory = this.deleteHistoryRepository.save(
			new DeleteHistory(ContentType.ANSWER, 1L, savedUser, LocalDateTime.now()));
	}

	@Test
	@DisplayName("deleteHistory save 테스트")
	void test() {
		assertThat(this.deleteHistory.getId()).isNotNull();
	}

	@Test
	@DisplayName("deleteHistory 제거 후 아이디로 찾았을때 값이 없음을 확인")
	void test_deleteAndFind() {
		this.deleteHistoryRepository.delete(this.deleteHistory);
		Optional<DeleteHistory> delHistoryOpt = this.deleteHistoryRepository.findById(deleteHistory.getId());
		assertThat(delHistoryOpt.isPresent()).isFalse();
	}

	@Test
	@DisplayName("deleteHistory 목록을 찾아 반환확인")
	void test_deleteHistoryList() {
		List<DeleteHistory> actual = this.deleteHistoryRepository.findAll();
		assertThat(actual.size()).isEqualTo(1);
	}
}