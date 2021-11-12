package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

	@Test
	@DisplayName("JAVAJIGI 에서 SANJIGI로 변경했을 때 email,name SANJIGI 동일")
	void given_JAVAJIGI_when_update_SANJIGI_then_equalsEmailAndName_SINJIGI_isTrue () {

		//when
		JAVAJIGI.update(JAVAJIGI,SANJIGI);

		assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isTrue();
	}

	@Test
	@DisplayName("JAVAJIGI 에서 SANJIGI로 변경했을 때 email,name SANJIGI 동일")
	void given_JAVAGIGI_then_matchPassword () {
		// then
		Assertions.assertThat(JAVAJIGI.matchPassword("password")).isTrue();
		Assertions.assertThat(JAVAJIGI.matchPassword("notMatchPassword")).isFalse();
	}
}
