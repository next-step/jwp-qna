package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class UserIdTest {

	@Test
	void 생성() {
		//given
		String text = "20바이트아이디";

		//when
		UserId 유저아이디 = UserId.generate(text);

		//then
		assertThat(유저아이디).isNotNull();
	}

	@Test
	void 생성_null_예외발생() {
		//given
		String null값 = null;

		//when

		//then
		assertThatThrownBy(() -> UserId.generate(null값))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_빈문자열_예외발생() {
		//given
		String 빈_문자열 = "";

		//when

		//then
		assertThatThrownBy(() -> UserId.generate(빈_문자열))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_20바이트_초과_예외발생() {
		//given
		String 이십일_바이트_유저아이디 = "21바이트_아이디";

		//when

		//then
		assertThatThrownBy(() -> UserId.generate(이십일_바이트_유저아이디))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		UserId 유저아이디 = UserId.generate("invinciblefunc");
		UserId 비교할_유저아이디 = UserId.generate("invinciblefunc");

		//when
		boolean 동일성여부 = 유저아이디.equals(비교할_유저아이디);

		//then
		assertThat(동일성여부).isTrue();
	}
}
