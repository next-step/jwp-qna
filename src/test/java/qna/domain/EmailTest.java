package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailTest {

	@Test
	@DisplayName("이메일 길이는 50을 넘지 않아야 한다")
	void validateTest1() {
		// given
		String emailText = "12345678901234567890123456789012345678901@naver.com";

		// when, then
		assertThatThrownBy(() -> Email.of(emailText))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("이메일은 이메일 양식에 맞아야 한다")
	void validateTest2() {
		// given
		String emailText = "123";

		// when, then
		assertThatThrownBy(() -> Email.of(emailText))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
