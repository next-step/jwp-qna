package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class NameTest {

	@Test
	void 생성() {
		//given
		String text = "jaeyoung lee";

		//when
		Name 이름 = Name.generate(text);

		//then
		assertThat(이름).isNotNull();
	}

	@Test
	void 생성_null_예외발생() {
		//given
		String null값 = null;

		//when

		//then
		assertThatThrownBy(() -> Name.generate(null값)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_빈문자열_예외발생() {
		//given
		String 빈_문자열 = "";

		//when

		//then
		assertThatThrownBy(() -> Name.generate(빈_문자열)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_20바이트_초과_예외발생() {
		//given
		String 이십일바이트_이름 = "이십일바이트명";

		//when

		//then
		assertThatThrownBy(() -> Name.generate(이십일바이트_이름))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 변경() {
		//given
		Name 이름 = Name.generate("jaeyoung lee");
		String 변경할_이름 = "LEE JAE YOUNG";

		//when
		이름.changeName(변경할_이름);

		//then
		assertThat(이름.value()).isEqualTo(변경할_이름);
	}

	@Test
	void 변경_null_예외발생() {
		//given
		Name 이름 = Name.generate("jaeyoung lee");
		String null값 = null;

		//when

		//then
		assertThatThrownBy(() -> 이름.changeName(null값)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 변경_빈문자열_예외발생() {
		//given
		Name 이름 = Name.generate("jaeyoung lee");
		String 빈_문자열 = "";

		//when

		//then
		assertThatThrownBy(() -> 이름.changeName(빈_문자열)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 변경_20바이트_초과_예외발생() {
		//given
		Name 이름 = Name.generate("jaeyoung lee");
		String 이십일바이트_이름 = "이십일바이트명";

		//when

		//then
		assertThatThrownBy(() -> 이름.changeName(이십일바이트_이름))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Name 이름 = Name.generate("LEE JAE YOUNG");
		Name 비교할_이름 = Name.generate("LEE JAE YOUNG");

		//when
		boolean 동일성여부 = 이름.equals(비교할_이름);

		//then
		assertThat(동일성여부).isTrue();
	}
}
