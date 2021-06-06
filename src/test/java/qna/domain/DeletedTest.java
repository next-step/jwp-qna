package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeletedTest {

	@Test
	@DisplayName("delete 수행시 true 설정 여부 확인")
	void testDelete() {
		Deleted deleted = new Deleted();
		deleted.delete();

		assertThat(deleted.isDeleted()).isTrue();
	}
}