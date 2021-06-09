package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User JAVAJIGI_CLONE = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@DisplayName("User : equals()")
	@Test
	@Order(1)
	void equals() {
		//given

		//when
		JAVAJIGI.equals(JAVAJIGI_CLONE);
		JAVAJIGI.equals(SANJIGI);

		//then
		assertAll(
			() -> assertThat(JAVAJIGI.equals(JAVAJIGI_CLONE)).isTrue(),
			() -> assertThat(JAVAJIGI.equals(SANJIGI)).isFalse()
		);
	}

	@DisplayName("User : update() - 유저아이디와 패스워드가 동일할 경우 이름과 이메일을 변경한다.")
	@Test
	@Order(2)
	void update() {
		//given

		//when
		JAVAJIGI_CLONE.changeEmail("test@test.com");
		JAVAJIGI_CLONE.changeName("테스터");
		JAVAJIGI.update(JAVAJIGI, JAVAJIGI_CLONE);

		//then
		assertAll(
			() -> assertThatCode(() -> JAVAJIGI.update(JAVAJIGI, JAVAJIGI_CLONE))
				.doesNotThrowAnyException(),
			() -> assertThat(JAVAJIGI.equals(JAVAJIGI_CLONE)).isTrue()
		);
	}

}
