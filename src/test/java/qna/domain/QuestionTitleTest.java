package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("질문 제목")
class QuestionTitleTest {
	@DisplayName("질문 제목을 생성할 수 있다.")
	@Test
	void of() {
		// given
		String value = "title";

		// when
		QuestionTitle title = QuestionTitle.of(value);

		// then
		assertThat(title).isNotNull();
	}

	@DisplayName("질문 제목은 100자 이내여야 한다.")
	@ParameterizedTest
	@EmptySource
	@NullSource
	@ValueSource(strings = {
		"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
	})
	void of_fail_too_long(String value) {
		// given & when & then
		assertThatThrownBy(() -> QuestionTitle.of(value))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
