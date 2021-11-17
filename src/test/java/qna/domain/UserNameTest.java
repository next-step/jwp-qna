package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserNameTest {

	@Test
	@DisplayName("길이 20을 초과할 수 없다")
	void validateTest() {
		String name = "123456789012345678901";

		assertThatThrownBy(() -> UserName.of(name))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
