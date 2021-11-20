package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoriesTest {

	@Test
	@DisplayName("여러 개를 추가할 수 있다")
	void addAllTest() {

		// given
		DeleteHistory d1 = new DeleteHistory(1L, ContentType.QUESTION, 1L, UserTest.SANJIGI);
		DeleteHistory d2 = new DeleteHistory(2L, ContentType.QUESTION, 2L, UserTest.JAVAJIGI);
		DeleteHistories deleteHistories = DeleteHistories.of();
		DeleteHistories others = DeleteHistories.of(Arrays.asList(d1, d2));
		DeleteHistories expected = DeleteHistories.of(Arrays.asList(d1, d2));

		// when
		deleteHistories = deleteHistories.combine(others);

		// then
		assertThat(deleteHistories).isEqualTo(expected);
	}

}
