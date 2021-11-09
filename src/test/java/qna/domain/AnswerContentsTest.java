package qna.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AnswerContentsTest {

	@DisplayName("AnswerContents 는 null or 빈문자열을 입력하여 생성할 수 있다.")
	@ParameterizedTest
	@NullAndEmptySource
	void create(String contents) {
		// when
		AnswerContents answerContents = AnswerContents.of(contents);

		// then
		assertTrue(answerContents.isEmpty());
	}
}