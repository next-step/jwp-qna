package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Test
	@DisplayName("저장하기 전후의 객체가 서로 동일한 객체인가")
	void save() {
		final Question question = Fixture.question("writer.id");
		final DeleteHistory expected = new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
		final DeleteHistory actual = deleteHistoryRepository.save(expected);
		assertThat(actual).isEqualTo(expected);
	}
}
