package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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

	@Test
	@DisplayName("ID가 주어지면 Answer를 삭제한다")
	void deleteTest() {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");

		// when
		DeleteHistory deleteHistory = a1.delete();

		// then
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

		assertAll(
			() -> assertThat(deleteHistory).isEqualTo(expected),
			() -> assertThat(a1.isDeleted()).isTrue()
		);
	}
	
}
