package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.ErrorMessage.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "javajigi", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "sanjigi", "sanjigi@slipp.net");

	@Test
	@DisplayName("동일한 유저의 계정을 수정시 변경되었는지 확인")
	void whenUpdateUserHavingAuthority_thenEqualsNameAndEmail_isTrue () {

		// when
		JAVAJIGI.update(JAVAJIGI,SANJIGI);

		// then
		assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isTrue();
	}

	@Test
	@DisplayName("동일한 유저아이디와 패스워드가 아닌경우 수정시 예외")
	void whenUpdateUser_UnAuthority_then_UnAuthorizedException () {

		String name = "seunghoona";
		User user = new User(3L, name, "password", name, "seunghoona@gmail.com");
		Assertions.assertThatThrownBy(() -> {
				// when
				JAVAJIGI.update(user, SANJIGI);
			}).isInstanceOf(UnAuthorizedException.class)
			.hasMessage(UN_AUTHORITY);
	}

	@Test
	@DisplayName("User1 에서 User2로 변경했을 때 email,name SANJIGI 동일")
	void given_User_then_matchPassword () {
		// then
		Assertions.assertThat(JAVAJIGI.matchPassword("password")).isTrue();
		Assertions.assertThat(JAVAJIGI.matchPassword("notMatchPassword")).isFalse();
	}

}
