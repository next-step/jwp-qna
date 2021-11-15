package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Test
	@DisplayName("ID가 같다면 객체도 같아야 한다")
	void equalsTest() {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer a2 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

		// when
		assertThat(a1).isEqualTo(a2)
			.hasSameHashCodeAs(a2);
	}

}
