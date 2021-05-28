package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository repository;

	private Answer save;

	@BeforeEach
	void setUp() {
		save = repository.save(AnswerTest.A1);
	}

	@Test
	@DisplayName("정답을 저장된 후, ID를 갖는다")
	void saveTest() {
		assertAll(
			() -> assertThat(save).isNotNull(),
			() -> assertThat(save.getId()).isNotNull()
		);
	}

	@Test
	@DisplayName("정답을 저장된 후, 원본은 같다")
	void findByIdTest() {
		Answer find = repository.findById(save.getId()).orElse(null);
		assertAll(
			() -> assertThat(find).isNotNull(),
			() -> assertThat(find).isEqualTo(save)
		);
	}
}
