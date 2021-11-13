package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("이메일")
class EmailTest {
	@DisplayName("이메일을 생성할 수 있다.")
	@ParameterizedTest
	@EmptySource
	@NullSource
	@ValueSource(strings = {
		"y2o2u2n@gmail.com"
	})
	void of(String value) {
		// given & when
		Email email = Email.of(value);

		// then
		assertThat(email).isNotNull();
	}

	@DisplayName("이메일은 이메일 형식을 따른다.")
	@ParameterizedTest
	@ValueSource(strings = {
		"notemailformat",
	})
	void of_fail_invalid_format(String value) {
		// given & when & then
		assertThatThrownBy(() -> Email.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("이메일은 길이가 50자를 넘을 수 없다.")
	@ParameterizedTest
	@ValueSource(strings = {
		"emailformat@buttoolonglonglonglonglonglonglonglong.com",
	})
	void of_fail_too_long(String value) {
		// given & when & then
		assertThatThrownBy(() -> Email.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
