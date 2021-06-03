package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.user.UserTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteHistorysTest {


	@Test
	@DisplayName("한개의 원소로 생성 테스트")
	void constructorTest() {
		assertThat(DeleteHistorys.of(DeleteHistory.newInstance(ContentType.QUESTION,1l, UserTest.JAVAJIGI)))
			.isNotNull()
			.extracting(value -> value.values().size())
			.isEqualTo(1);
	}

	@Test
	@DisplayName("복수의 원소를 가진 리스트로 생성 테스트")
	void constructorTestWithListParameter() {
		// given
		List<DeleteHistory> expected = Arrays.asList(DeleteHistory.newInstance(ContentType.QUESTION, 1l, UserTest.JAVAJIGI),
													 DeleteHistory.newInstance(ContentType.QUESTION, 2l, UserTest.SANJIGI));

		// when
		DeleteHistorys actual = DeleteHistorys.of(expected);

		// then
		assertThat(actual).isNotNull();
		assertThat(actual.values())
			.hasSize(expected.size())
			.containsAll(expected);

	}

	@Test
	@DisplayName("두개의 DeleteHistorys를 합치는 addAll 테스트")
	void addAllTest() {
		// given
		DeleteHistorys deleteHistorys = DeleteHistorys.of(DeleteHistory.newInstance(ContentType.QUESTION, 1l, UserTest.JAVAJIGI));
		List<DeleteHistory> expected = Arrays.asList(DeleteHistory.newInstance(ContentType.QUESTION,1l, UserTest.JAVAJIGI),
													 DeleteHistory.newInstance(ContentType.QUESTION,2l, UserTest.SANJIGI));

		// when
		deleteHistorys.addAll(DeleteHistorys.of(expected));

		// then
		assertThat(deleteHistorys.values())
			.hasSize(3)
			.containsAll(expected);
	}

}
