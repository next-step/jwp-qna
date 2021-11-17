package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserEntityIdTest {

	@Test
	@DisplayName("길이가 20 초과해서는 안된다")
	void validateTest() {
		// given
		String text = "123456789012345678901";

		// when, then
		assertThatThrownBy(() -> UserId.of(text))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
