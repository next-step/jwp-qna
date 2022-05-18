package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {
	@Test
	@DisplayName("생성 테스트")
	void create() {
		assertAll(() -> new DeleteHistory(ContentType.ANSWER, AnswerTest.A1.getId(), UserTest.JAVAJIGI.getId(),LocalDateTime.now()),
				() -> new DeleteHistory(ContentType.QUESTION, QuestionTest.Q1.getId(), UserTest.SANJIGI.getId(),LocalDateTime.now()));
	}
	
}
