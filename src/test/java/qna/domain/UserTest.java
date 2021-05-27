package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	private User user;

	@BeforeEach
	void setup() {
		user = new User("test", "test", "test", "test");
	}

	@Test
	@DisplayName("matchPassword를 테스트")
	void testMatchPassword() {
		Assertions.assertThat(JAVAJIGI.matchPassword("password")).isTrue();
		Assertions.assertThat(JAVAJIGI.matchPassword("password1")).isFalse();
	}

	@Test
	@DisplayName("이름과 이메일이 같은지 테스트")
	void testEqualsNameAndEmail() {
		Assertions.assertThat(JAVAJIGI.equalsNameAndEmail(SANJIGI)).isFalse();
		Assertions.assertThat(JAVAJIGI.equalsNameAndEmail(null)).isFalse();
		Assertions.assertThat(JAVAJIGI.equalsNameAndEmail(JAVAJIGI)).isTrue();
	}

	@Test
	@DisplayName("업데이트 테스트")
	void testUpdate() {
		user.update(user, new User("test1", "test", "test1", "test1"));

		Assertions.assertThat(user.getName()).isEqualTo("test1");
		Assertions.assertThat(user.getEmail()).isEqualTo("test1");
	}

	@Test
	@DisplayName("업데이트 대상과 로그인 대상이 다른경우 업데이트 오류발생 테스트")
	void testUpdateNotLogin() {
		Assertions.assertThatThrownBy(() -> {
			user.update(JAVAJIGI, new User("test1", "test", "test1", "test1"));
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("업데이트 대상과 업데이트 유저의 비밀번호가 다른경우 오류발생 테스트")
	void testUpdateMissMatchPassword() {
		Assertions.assertThatThrownBy(() -> {
			user.update(JAVAJIGI, new User("test1", "test2", "test1", "test1"));
		}).isInstanceOf(UnAuthorizedException.class);
	}
}
