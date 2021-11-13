package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("사용자 이름")
class UserNameTest {
	@DisplayName("사용자 이름을 생성할 수 있다.")
	@Test
	void of() {
		// given
		String value = "youn";

		// when
		UserName userName = UserName.of(value);

		// then
		assertThat(userName).isNotNull();
	}

	@DisplayName("사용자 이름을 20자 이내여야 한다.")
	@ParameterizedTest
	@EmptySource
	@NullSource
	@ValueSource(strings = {
		"111111111111111111111",
	})
	void of_fail_too_long(String value) {
		// given & when & then
		assertThatThrownBy(() -> UserName.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
