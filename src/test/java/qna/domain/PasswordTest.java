package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordTest {

	@Test
	@DisplayName("길이를 20 초과할 수 없다")
	void validateTest() {
		String text = "123456789012345678901";

		assertThatThrownBy(() -> Password.of(text))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("null 이면 안된다")
	void validateNullTest() {
		String text = null;

		assertThatThrownBy(() -> Password.of(text))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
