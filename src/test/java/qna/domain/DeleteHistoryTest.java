package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

	@Test
	@DisplayName("같은 값을 지닌 객체는 같아야 한다")
	void equalsTest() {
		// given
		DeleteHistory h1 = new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI);
		DeleteHistory h2 = new DeleteHistory(1L, ContentType.ANSWER, 1L, UserTest.JAVAJIGI);

		// when, then
		assertThat(h1).isEqualTo(h2)
			.hasSameHashCodeAs(h2);
	}

	@Test
	@DisplayName("Content 정보만을 가지고 생성할 수 있다")
	void constructorTest() {
		// given
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

		// when
		DeleteHistory result = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());

		// then
		assertThat(result).isEqualTo(expected);
	}

}
