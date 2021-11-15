package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("ID가 같다면 객체도 같아야 한다")
	void equalsTest() {
		// given
		Question q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Question q2 = new Question(1L, "title2", "contents2").writeBy(UserTest.JAVAJIGI);

		// when, then
		assertThat(q1).isEqualTo(q2)
			.hasSameHashCodeAs(q2);
	}

}
