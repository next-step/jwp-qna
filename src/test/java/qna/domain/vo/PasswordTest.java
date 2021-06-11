package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class PasswordTest {

	@Test
	void 생성() {
		//given
		String text = "password";

		//when
		Password 비밀번호 = Password.generate(text);

		//then
		assertThat(비밀번호).isNotNull();
	}

	@Test
	void 생성_null_예외발생() {
		//given
		String null값 = null;

		//when

		//then
		assertThatThrownBy(() -> Password.generate(null값))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_빈문자열_예외발생() {
		//given
		String 빈_문자열 = "";

		//when

		//then
		assertThatThrownBy(() -> Password.generate(빈_문자열))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_20바이트_초과_예외발생() {
		//given
		String 이십일바이트_비밀번호 = "21바이트_한글임";

		//when

		//then
		assertThatThrownBy(() -> Password.generate(이십일바이트_비밀번호))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Password 비밀번호 = Password.generate("P@ssw0Rd");
		Password 비교할_비밀번호 = Password.generate("P@ssw0Rd");

		//when
		boolean 동일성여부 = 비밀번호.equals(비교할_비밀번호);

		//then
		assertThat(동일성여부).isTrue();
	}
}
