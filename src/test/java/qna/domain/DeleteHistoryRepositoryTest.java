package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Autowired
	private EntityManager entityManager;

	private DeleteHistory deleteHistory;

	@BeforeEach
	void setup() {
		deleteHistory = new DeleteHistory();
		deleteHistories.save(deleteHistory);
	}

	@Test
	@DisplayName("insert 되면 id 가 자동 생성 되어야 한다.")
	void saveTest() {
		assertThat(deleteHistory.getId()).isNotNull();
	}

	@Test
	@DisplayName("delete 가 되면 해당 엔티티를 찾을 수 없어야 한다.")
	void deleteTest() {
		deleteHistories.delete(deleteHistory);
		clearPersistenceContext();

		Optional<DeleteHistory> notExistHistory = deleteHistories.findById(deleteHistory.getId());

		assertThat(notExistHistory).isEmpty();
	}

	private void clearPersistenceContext() {
		entityManager.flush();
		entityManager.clear();
	}
}