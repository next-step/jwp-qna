package qna.domain;

import static qna.domain.DeleteHistoryTest.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Test
	void 저장된객체와_리턴하는객체는_동일하다() {
		DeleteHistory save = deleteHistoryRepository.save(DELETE_HISTORY1);
		Assertions.assertThat(save.equals(DELETE_HISTORY1)).isTrue();
	}
}
