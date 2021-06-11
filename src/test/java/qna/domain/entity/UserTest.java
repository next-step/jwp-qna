package qna.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.UnAuthorizedException;

public class UserTest {

	private User 자바지기;
	private User 복제된_자바지기;

	@BeforeEach
	void 초기화() {
		자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
		복제된_자바지기 = User.generate(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
	}

	@Test
	void 생성() {
		//given

		//when
		User 산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");

		//then
		assertThat(산지기).isNotNull();
	}

	@Test
	void 생성_아이디_PK_없음() {
		//given

		//when
		User 산지기 = User.generate(null, "sanjigi", "password2", "name2", "sanjigi@slipp.net");

		//then
		assertThat(산지기).isNotNull();
	}

	@Test
	void 생성_유저아이디_null_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, null, "password2", "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_유저아이디_빈문자열_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, "", "password2", "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_유저아이디_20바이트_초과_예외발생() {
		//given

		//when
		String 유저아이디_20바이트_초과 = "abcdefghijabcdefghija";

		//then
		assertThatThrownBy(() -> User.generate(2L, 유저아이디_20바이트_초과, "password2", "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_비밀번호_null_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", null, "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_비밀번호_빈문자열_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", "", "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_비밀번호_20바이트_초과_예외발생() {
		//given

		//when
		String 비밀번호_20바이트_초과 = "password12password123";

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", 비밀번호_20바이트_초과, "name2", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_이름_null_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", "password2", null, "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_이름_빈문자열_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", "password2", "", "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_이름_20바이트_초과_예외발생() {
		//given

		//when
		String 이름_20바이트_초과 = "이건이름맞아요";

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", "password2", 이름_20바이트_초과, "sanjigi@slipp.net"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_이메일_null() {
		//given

		//when
		User 산지기 = User.generate(null, "sanjigi", "password2", "name2", null);

		//then
		assertThat(산지기).isNotNull();
	}

	@Test
	void 생성_이메일_빈문자열() {
		//given

		//when
		User 산지기 = User.generate(null, "sanjigi", "password2", "name2", "");

		//then
		assertThat(산지기).isNotNull();
	}

	@Test
	void 생성_이메일_50바이트_초과_예외발생() {
		//given

		//when
		String 이메일_50바이트_초과 = "abcdefghijabcdefghijabcdefghijabcdefghija@naver.com";

		//then
		assertThatThrownBy(() -> User.generate(2L, "sanjigi", "password2", "sanjigi", 이메일_50바이트_초과))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 이름변경_null_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> 자바지기.changeName(null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 이름변경_빈문자열_예외발생() {
		//given

		//when

		//then
		assertThatThrownBy(() -> 자바지기.changeName("")).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 이름변경_20바이트_초과_예외발생() {
		//given

		//when
		String 이름_20바이트_초과 = "이건이름맞아요";

		//then
		assertThatThrownBy(() -> 자바지기.changeName(이름_20바이트_초과)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 이메일변경_null() {
		//given

		//when
		자바지기.changeEmail(null);

		//then
		assertThat(자바지기.email()).isNull();
	}

	@Test
	void 이메일변경_빈문자열() {
		//given

		//when
		자바지기.changeEmail("");

		//then
		assertThat(자바지기.email()).isEqualTo("");
	}

	@Test
	void 이메일변경_50바이트_초과_예외발생() {
		//given

		//when
		String 이메일_50바이트_초과 = "abcdefghijabcdefghijabcdefghijabcdefghija@naver.com";

		//then
		assertThatThrownBy(() -> 자바지기.changeEmail(이메일_50바이트_초과)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		User 산지기 = User.generate(2L, "sanjigi", "password2", "name2",
			"sanjigi@slipp.net");

		//when

		//then
		assertAll(
			() -> assertThat(자바지기.equals(복제된_자바지기)).isTrue(),
			() -> assertThat(자바지기.equals(산지기)).isFalse()
		);
	}

	@Test
	void 이메일이름변경() {
		//given
		User 로그인한_자바지기 = 자바지기;
		User 변경된_자바지기 = 복제된_자바지기;

		//when
		변경된_자바지기.changeEmail("test@test.com");
		변경된_자바지기.changeName("테스터");
		자바지기.update(로그인한_자바지기, 변경된_자바지기);

		//then
		assertThat(자바지기.equals(복제된_자바지기)).isTrue();
	}

	@Test
	void 이메일이름변경_로그인한_유저_다름_예외발생() {
		//given
		User 로그인한_산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");
		User 변경된_자바지기 = 복제된_자바지기;

		//when
		변경된_자바지기.changeEmail("test@test.com");
		변경된_자바지기.changeName("테스터");

		//then
		assertThatThrownBy(() -> 자바지기.update(로그인한_산지기, 변경된_자바지기)).isInstanceOf(UnAuthorizedException.class);
	}


	@Test
	void 이메일이름변경_변경된_유저_다름_예외발생() {
		//given
		User 로그인한_자바지기 = 복제된_자바지기;
		User 변경된_산지기 = User.generate(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");

		//when
		변경된_산지기.changeEmail("test@test.com");
		변경된_산지기.changeName("테스터");

		//then
		assertThatThrownBy(() -> 자바지기.update(로그인한_자바지기, 변경된_산지기)).isInstanceOf(UnAuthorizedException.class);
	}

}
