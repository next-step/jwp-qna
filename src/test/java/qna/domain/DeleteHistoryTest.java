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
	private DeleteHistoryRepository deleteHistoryRepository;

	public static final DeleteHistory DH1 = new DeleteHistory(ContentType.ANSWER, 1L, 1L,
		LocalDateTime.of(2021, 6, 2, 22, 30));
	public static final DeleteHistory DH2 = new DeleteHistory(ContentType.QUESTION, 2L, 2L,
		LocalDateTime.of(2021, 6, 2, 23, 10));

	@Test
	@DisplayName("jpa between 조회")
	void select_between() {
		DeleteHistory saveDH1 = deleteHistoryRepository.save(DH1);
		DeleteHistory saveDH2 = deleteHistoryRepository.save(DH2);

		assertThat(
			deleteHistoryRepository.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 22, 40)).size()).isEqualTo(
			1);
		assertThat(
			deleteHistoryRepository.findByCreateDateBetween(LocalDateTime.of(2021, 6, 2, 22, 10),
				LocalDateTime.of(2021, 6, 2, 23, 40)).size()).isEqualTo(
			2);
	}
}
