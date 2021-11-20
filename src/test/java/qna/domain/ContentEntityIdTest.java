package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContentEntityIdTest {

	@Test
	@DisplayName("같은 값을 가지면 같아야 한다")
	void equalsTest() {
		// given
		Long id = 1L;
		ContentId expected = ContentId.of(id);

		// when
		ContentId contentId = ContentId.of(id);

		// then
		assertThat(contentId).isEqualTo(expected);
	}
}
