package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

	private static final String EMPTY_ENTITY_MESSAGE = "찾는 Entity가 없습니다.";

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;


	@Test
	@DisplayName("save test")
	void saveTest() {
		// given
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 0l, 0l, LocalDateTime.now());

		// when
		assertThat(deleteHistoryRepository.save(deleteHistory))
			.isNotNull()
			.isSameAs(deleteHistory); // then
	}

	@Test
	@DisplayName("findById test")
	void findByIdTest() {
		// given
		DeleteHistory deleteHistory = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, 0l, 0l, LocalDateTime.now()));

		// when
		assertThat(deleteHistoryRepository.findById(deleteHistory.getId())
										  .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull() // then
			.isSameAs(deleteHistory);
	}

}
