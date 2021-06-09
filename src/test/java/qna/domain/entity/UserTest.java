package qna.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

	private User javajigi;
	private User clonedJavajigi;

	@BeforeEach
	void initialize() {
		javajigi = User.generate(1L, "javajigi", "password1", "name1",
			"javajigi@slipp.net");
		clonedJavajigi = User.generate(1L, "javajigi", "password1", "name1",
			"javajigi@slipp.net");
	}

	@DisplayName("User : equals()")
	@Test
	void equals() {
		//given
		User sanjigi = User.generate(2L, "sanjigi", "password2", "name2",
			"sanjigi@slipp.net");

		//when

		//then
		assertAll(
			() -> assertThat(javajigi.equals(clonedJavajigi)).isTrue(),
			() -> assertThat(javajigi.equals(sanjigi)).isFalse()
		);
	}

	@DisplayName("User : update() - 유저아이디와 패스워드가 동일할 경우 이름과 이메일을 변경한다.")
	@Test
	void update() {
		//given

		//when
		clonedJavajigi.changeEmail("test@test.com");
		clonedJavajigi.changeName("테스터");
		javajigi.update(javajigi, clonedJavajigi);

		//then
		assertAll(
			() -> assertThatCode(() -> javajigi.update(javajigi, clonedJavajigi))
				.doesNotThrowAnyException(),
			() -> assertThat(javajigi.equals(clonedJavajigi)).isTrue()
		);
	}

}
