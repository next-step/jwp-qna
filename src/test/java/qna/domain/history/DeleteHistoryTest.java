package qna.domain.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.domain.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeleteHistoryTest {

	@Test
	@DisplayName("Question contentType으로 생성하는 정적메서드 테스트")
	void newInstanceOfQuestionTest() {
		// when
		DeleteHistory deleteHistory = DeleteHistory.newInstance(ContentType.QUESTION, 1L, UserTest.JAVAJIGI);

		assertAll(() -> {
			assertThat(deleteHistory).isNotNull();
			assertThat(deleteHistory.isSameContent(ContentType.QUESTION));
			assertThat(deleteHistory.isSameOwner(UserTest.JAVAJIGI));
		});
	}

	@Test
	@DisplayName("Answer contentType으로 생성하는 정적메서드 테스트")
	void newInstanceOfAnswerTest() {
		// when
		DeleteHistory deleteHistory = DeleteHistory.newInstance(ContentType.ANSWER, 1L, UserTest.JAVAJIGI);

		assertAll(() -> {
			assertThat(deleteHistory).isNotNull();
			assertThat(deleteHistory.isSameContent(ContentType.ANSWER));
			assertThat(deleteHistory.isSameOwner(UserTest.JAVAJIGI));
		});
	}
}
