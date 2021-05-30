package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

	private static final String EMPTY_ENTITY_MESSAGE = "찾는 Entity가 없습니다.";

	@Autowired
	private DeleteHistoryRepository deleteHistorys;


	@Test
	@DisplayName("save test")
	void saveTest() {
		// given
		DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, 0l, 0l, LocalDateTime.now());

		// when
		assertThat(deleteHistorys.save(deleteHistory))
			.isNotNull()
			.isSameAs(deleteHistory); // then
	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// given
		DeleteHistory expected = deleteHistorys.save(new DeleteHistory(ContentType.ANSWER, 0l, 0l, LocalDateTime.now()));

		// when
		expected.setContentType(ContentType.QUESTION);

		// then
		assertThat(deleteHistorys.findById(expected.getId())
								 .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull()
			.extracting(value -> value.getContentType())
			.isEqualTo(ContentType.QUESTION);
	}

	@Test
	@DisplayName("findById test")
	void findByIdTest() {
		// given
		DeleteHistory deleteHistory = deleteHistorys.save(new DeleteHistory(ContentType.ANSWER, 0l, 0l, LocalDateTime.now()));

		// when
		assertThat(deleteHistorys.findById(deleteHistory.getId())
								 .orElseThrow(() -> new NullPointerException(EMPTY_ENTITY_MESSAGE)))
			.isNotNull() // then
			.isSameAs(deleteHistory);
	}

}
