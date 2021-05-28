package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Test
	@DisplayName("User 생성")
	void create() {
		User user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		assertThat(user).isNotNull();
		assertThat(user.getUserId()).isEqualTo("javajigi");
		assertThat(user.getEmail()).isEqualTo("javajigi@slipp.net");
		assertThat(user.getName()).isEqualTo("name");
		assertThat(user.getPassword()).isEqualTo("password");
	}

	@Test
	@DisplayName("User 수정")
	void update() {
		User loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User targetUser = new User(1L, "javajigi", "password", "kwaktaemin", "taminging@kakao.com");
		loginUser.update(loginUser, targetUser);
		assertThat(loginUser).isNotNull();
		assertThat(loginUser.getUserId()).isEqualTo("javajigi");
		assertThat(loginUser.getEmail()).isEqualTo("taminging@kakao.com");
		assertThat(loginUser.getName()).isEqualTo("kwaktaemin");
		assertThat(loginUser.getPassword()).isEqualTo("password");
	}

	@Test
	@DisplayName("User 수정 시 UnAuthorizedException 테스트(패스워드가 다를 경우")
	void validateUpdate_패스워드가_다른_경우_UnAuthorizedException() {
		User loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User targetUser = new User(1L, "javajigi", "123", "kwaktaemin", "taminging@kakao.com");
		assertThatThrownBy(() ->
			loginUser.update(loginUser, targetUser)
		).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("User 수정 시 UnAuthorizedException 테스트(아이디가 다를 경우")
	void validateUpdate_아이디가_다른_경우_UnAuthorizedException() {
		User loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User targetUser = new User(1L, "kwaktaemin", "password", "kwaktaemin", "taminging@kakao.com");
		assertThatThrownBy(() ->
			loginUser.update(targetUser, targetUser)
		).isInstanceOf(UnAuthorizedException.class);
	}
}
