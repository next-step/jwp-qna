package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("사용자 비밀번호")
class UserPasswordTest {
	@DisplayName("사용자 비밀번호를 생성할 수 있다.")
	@Test
	void of() {
		// given
		String value = "password";

		// when
		UserPassword password = UserPassword.of(value);

		// then
		assertThat(password).isNotNull();
	}

	@DisplayName("사용자 비밀번호는 20자 이내여아 한다.")
	@ParameterizedTest
	@EmptySource
	@NullSource
	@ValueSource(strings = {
		"passwordpasswordpassword",
	})
	void of_fail_too_long(String value) {
		// given & when & then
		assertThatThrownBy(() -> UserPassword.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
