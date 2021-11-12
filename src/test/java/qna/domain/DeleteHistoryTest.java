package qna.domain;

import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import qna.TypeNotFoundException;

public class DeleteHistoryTest {
	public static DeleteHistory DELETE_HISTORY1 = new DeleteHistory(ContentType.QUESTION, Q1.getId(),
		JAVAJIGI.getId(),
		LocalDateTime.now());
	DeleteHistory DELETE_HISTORY2 = new DeleteHistory(ContentType.ANSWER, Q2.getId(), JAVAJIGI.getId(),
		LocalDateTime.now().plusMinutes(10));

	@Test
	void ContentType_Null이면_예외() {
		Assertions.assertThatThrownBy(() -> {
				new DeleteHistory(null, Q1.getId(), JAVAJIGI.getId(), LocalDateTime.now());
			}).isInstanceOf(TypeNotFoundException.class)
			.hasMessageContaining(ContentType.valuesString());
	}
}
