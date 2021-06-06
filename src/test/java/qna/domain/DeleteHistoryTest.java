package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
	@Autowired
	private DeleteHistoryRepository deleteHistories;

	@Test
	@DisplayName("jpa between 조회")
	void select_between() {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
			LocalDateTime.of(2021, 6, 2, 22, 30));
		DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, 2L,
			LocalDateTime.of(2021, 6, 2, 23, 10));

		DeleteHistory saveDH1 = deleteHistories.save(DH1);
		DeleteHistory saveDH2 = deleteHistories.save(DH2);

		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 22, 40)).size()).isEqualTo(
			1);
		assertThat(
			deleteHistories.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 23, 40)).size()).isEqualTo(
			2);
	}

	@Test
	@DisplayName("jpa less than 조회")
	void select_less_than() {
		DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
			LocalDateTime.of(2021, 6, 2, 22, 30));
		DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, 2L,
			LocalDateTime.of(2021, 6, 2, 23, 10));

		DeleteHistory saveDH1 = deleteHistories.save(DH1);
		DeleteHistory saveDH2 = deleteHistories.save(DH2);

		assertThat(
			deleteHistories.findByIdLessThan(saveDH2.getId()).get(0)).isEqualTo(saveDH1
		);
	}
}
