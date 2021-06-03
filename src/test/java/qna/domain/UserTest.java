package qna.domain;

import org.junit.jupiter.api.Test;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");


	@Test
	public void 사용자_정보수정_테스트_GREEN () {
		User user = new User(3L, "maeve", "password", "name", "maeve.woo@cyworldlabs.com");
		user.update(user, new User("maeve", "password", "changed", "accidentlywoo@gmail.com"));

		assertThat(user.name()).isEqualTo("changed");
		assertThat(user.email()).isEqualTo("accidentlywoo@gmail.com");
	}

	@Test
	public void 사용자_정보수정_테스트_RED_id_missmatch () {
		User user = new User(3L, "maeve", "password", "name", "maeve.woo@cyworldlabs.com");

		assertThatThrownBy(() -> {
			user.update(new User("miss", "password", "name", "maeve.woo@cyworldlabs.com"), user);
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	public void 사용자_정보수정_테스트_RED_password_missmatch () {
		User user = new User(3L, "maeve", "password", "name", "maeve.woo@cyworldlabs.com");

		assertThatThrownBy(() -> {
			user.update(user, new User("maeve", "miss", "changed", "accidentlywoo@gmail.com"));
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	public void 사용자_이름_확인_RED () {
		User user = new User(3L, "maeve", "password", "name", "maeve.woo@cyworldlabs.com");

		assertThat(user.equalsName(null)).isFalse();
		assertThat(user.equalsName(new User(3L, "maeve", "password", "miss", "maeve.woo@cyworldlabs.com"))).isFalse();
	}

	@Test
	public void 사용자_이름_확인_GREEN () {
		User user = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

		assertThat(user.equalsName(new User(3L, "maeve", "a", "maeve", "a@cyworldlabs.com"))).isTrue();
	}

	@Test
	public void 사용자_이메일_확인_RED () {
		User user = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

		assertThat(user.equalsEmail(new User(3L, "maeve", "a", "maeve", "maeve.woo@cyworldlabs.com"))).isTrue();
	}
}
