package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("사용자 ID")
class UserIdTest {

	@DisplayName("사용자 ID를 생성할 수 있다.")
	@Test
	void of() {
		// given
		String value = "y2o2u2n";

		// when
		UserId userId = UserId.of(value);

		// then
		assertThat(userId).isNotNull();
	}

	@DisplayName("사용자 ID를 생성할 수 없다.")
	@ParameterizedTest
	@EmptySource
	@NullSource
	@ValueSource(strings = {
		"111111111111111111111",
	})
	void of_fail(String value) {
		// given & when & then
		assertThatThrownBy(() -> UserId.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
