package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

	@Test
	@DisplayName("ID가 같다면 객체도 같아야 한다")
	void equalsTest() {
		// given
		DeleteHistory h1 = new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
		DeleteHistory h2 = new DeleteHistory(1L, ContentType.QUESTION, 2L, UserTest.SANJIGI);

		// when, then
		assertThat(h1).isEqualTo(h2)
			.hasSameHashCodeAs(h2);
	}
}
