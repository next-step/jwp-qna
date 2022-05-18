package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.UnAuthorizedException;

public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

	@Test
	@DisplayName("아이디 값이 존재하지않는 생성 테스트")
	void create_none_id_test() {
		User eastStar1129 = new User("eastStar1129", "password", "JangDongGyu", "eaststar1129@abcd.com");
		assertAll(() -> assertNull(eastStar1129.getId()), 
				() -> assertEquals(eastStar1129.getUserId(), "eastStar1129"),
				() -> assertTrue(eastStar1129.matchPassword("password")),
				() -> assertEquals(eastStar1129.getName(), "JangDongGyu"),
				() -> assertEquals(eastStar1129.getEmail(), "eaststar1129@abcd.com"));
	}

	@Test
	@DisplayName("생성 테스트")
	void create_test() {
		User eastStar1129 = new User(3L, "eastStar1129", "password", "JangDongGyu", "eaststar1129@abcd.com");
		assertAll(() -> assertEquals(eastStar1129.getId(), 3L),
				() -> assertEquals(eastStar1129.getUserId(), "eastStar1129"),
				() -> assertTrue(eastStar1129.matchPassword("password")),
				() -> assertEquals(eastStar1129.getName(), "JangDongGyu"),
				() -> assertEquals(eastStar1129.getEmail(), "eaststar1129@abcd.com"));
	}

	@Test
	@DisplayName("유저정보 업데이트 테스트(실패추가)")
	void update_user_test() {
		User javajigiLogin = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User updateJavajigiInfo = new User(1L, "javajigi", "password", "name2", "javajigi2@slipp.net");
		User hacker = new User(1L, "javajigi", "password2", "name", "javajigi@slipp.net");
		assertAll(() -> assertThrows(UnAuthorizedException.class, () -> JAVAJIGI.update(JAVAJIGI, hacker)),
				() -> JAVAJIGI.update(javajigiLogin, updateJavajigiInfo));
	}

	@Test
	@DisplayName("이름과 이메일이 모두일치하는지 확인")
	void equals_name_and_email() {
		User javajigiLogin = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		User eastStar1129 = new User("eastStar1129", "password", "JangDongGyu", "eaststar1129@abcd.com");
		assertAll(() -> assertTrue(JAVAJIGI.equalsNameAndEmail(javajigiLogin)),
				() -> assertFalse(JAVAJIGI.equalsNameAndEmail(eastStar1129)));
	}

	@Test
	@DisplayName("게스트 유저 테스트")
	void is_guest_user() {
		User guestUser = User.GUEST_USER;
		assertAll(() -> assertFalse(JAVAJIGI.isGuestUser()), () -> assertTrue(guestUser.isGuestUser()));
	}
}
