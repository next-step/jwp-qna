package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {
	public static DeleteHistory DELETE_HISTORY1 = new DeleteHistory(ContentType.QUESTION, Q1.getId(),
		UserTest.JAVAJIGI.getId(),
		LocalDateTime.now());
	DeleteHistory DELETE_HISTORY2 = new DeleteHistory(ContentType.ANSWER, Q2.getId(), UserTest.JAVAJIGI.getId(),
		LocalDateTime.now().plusMinutes(10));

	@Test
	void not_equals() {
		assertThat(DELETE_HISTORY1.equals(DELETE_HISTORY2)).isFalse();
	}
}
