package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class EmailTest {

	@Test
	void 생성() {
		//given
		String text = "abc@naver.com";

		//when
		Email 이메일 = Email.generate(text);

		//then
		assertThat(이메일).isNotNull();
	}

	@Test
	void 생성_null() {
		//given
		String null값 = null;

		//when
		Email 이메일 = Email.generate(null값);

		//then
		assertThat(이메일).isNotNull();
	}

	@Test
	void 생성_빈문자열() {
		//given
		String 빈_문자열 = "";

		//when
		Email 이메일 = Email.generate(빈_문자열);

		//then
		assertThat(이메일).isNotNull();
	}

	@Test
	void 생성_50바이트_초과_예외발생() {
		//given
		String 이메일 = "abcdefghijabcdefghijabcdefghijabcdefghija@naver.com";

		//when

		//then
		assertThatThrownBy(() -> Email.generate(이메일)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 변경() {
		//given
		Email 이메일 = Email.generate("abc@naver.com");
		String 변경할_메일주소 = "invinciblefunc@gmail.com";

		//when
		이메일.changeEmail(변경할_메일주소);

		//then
		assertThat(이메일.value()).isEqualTo(변경할_메일주소);
	}

	@Test
	void 변경_null() {
		//given
		Email 이메일 = Email.generate("invinciblefunc@gmail.com");
		String null값 = null;

		//when
		이메일.changeEmail(null값);

		//then
		assertThat(이메일.value()).isEqualTo(null값);
	}

	@Test
	void 변경_빈문자열() {
		//given
		Email 이메일 = Email.generate("invinciblefunc@gmail.com");
		String 빈_문자열 = "";

		//when
		이메일.changeEmail(빈_문자열);

		//then
		assertThat(이메일.value()).isEqualTo(빈_문자열);
	}

	@Test
	void 변경_50바이트_초과_예외발생() {
		//given
		Email 이메일 = Email.generate("invinciblefunc@gmail.com");
		String 변경할_메일주소_50바이트_초과 = "abcdefghijabcdefghijabcdefghijabcdefghija@naver.com";

		//when

		//then
		assertThatThrownBy(() -> 이메일.changeEmail(변경할_메일주소_50바이트_초과))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Email 이메일 = Email.generate("invinciblefunc@gmail.com");
		Email 비교할_이메일 = Email.generate("invinciblefunc@gmail.com");

		//when
		boolean 동일성여부 = 이메일.equals(비교할_이메일);

		//then
		assertThat(동일성여부).isTrue();
	}
}
