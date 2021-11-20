package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TitleTest {

	@Test
	@DisplayName("제목은 100자 이하여야 한다")
	void maxLengthTest() {
		// given
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 11; i++) {
			text.append("1234567890");
		}

		// when, then
		assertThatThrownBy(() -> Title.of(text.toString()))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
