package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTitle.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class QuestionTitleTest {

	@DisplayName("QuestionTitle 은 내부에 null 값을 가질 수 없다.")
	@ParameterizedTest
	@NullSource
	void create1(String contents) {
		// when & then
		assertThatThrownBy(() -> QuestionTitle.of(contents)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("QuestionTitle 은 내부에 0~100자인 값을 가질 수 있다.")
	@ParameterizedTest
	@ValueSource(strings = {"", "a",
		"012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678",
		"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"})
	void create2(String contents) {
		// when
		QuestionTitle questionTitle = QuestionTitle.of(contents);

		// then
		assertAll(
			() -> assertThat(questionTitle.getLength()).isGreaterThanOrEqualTo(CONTENTS_MIN_LENGTH),
			() -> assertThat(questionTitle.getLength()).isLessThanOrEqualTo(CONTENTS_MAX_LENGTH)
		);
	}

	@DisplayName("QuestionTitle 은 내부에 100자를 초과하는 값을 가질 수 없다.")
	@ParameterizedTest
	@ValueSource(strings = {
		"01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890",
		"012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901",
		"0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012"})
	void create3(String contents) {
		// when & then
		assertThatThrownBy(() -> QuestionTitle.of(contents)).isInstanceOf(IllegalArgumentException.class);
	}
}