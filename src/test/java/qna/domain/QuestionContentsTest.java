package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("질문 내용")
class QuestionContentsTest {
	@DisplayName("질문 내용을 생성할 수 있다.")
	@Test
	void of() {
		// given
		String value = "contents";

		// when
		QuestionContents contents = QuestionContents.of(value);

		// then
		assertThat(contents).isNotNull();
	}
}
