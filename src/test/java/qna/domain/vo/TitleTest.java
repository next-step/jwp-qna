package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

public class TitleTest {

	@Test
	void 생성() {
		//given
		String text = "지금 보시는 제목은 100바이트 제목입니다. 정말 100바이트 꽉 채운 제목이죠";

		//when
		Title 제목 = Title.generate(text);

		//then
		assertThat(제목).isNotNull();
	}

	@Test
	void 생성_null_예외발생() {
		//given
		String null값 = null;

		//when

		//then
		assertThatThrownBy(() -> Title.generate(null값))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_빈문자열_예외발생() {
		//given
		String 빈_문자열 = "";

		//when

		//then
		assertThatThrownBy(() -> Title.generate(빈_문자열))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 생성_100바이트_초과_예외발생() {
		//given
		String 백일바이트_제목 = "지금 보시는 제목은 100바이트 초과하는 제목입니다. 101바이트짜리 제목이죠";

		//when

		//then
		assertThatThrownBy(() -> Title.generate(백일바이트_제목))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Title 제목 = Title.generate("지금 보시는 제목은 100바이트 제목입니다. 정말 100바이트 꽉 채운 제목이죠");
		Title 비교할_제목 = Title.generate("지금 보시는 제목은 100바이트 제목입니다. 정말 100바이트 꽉 채운 제목이죠");

		//when
		boolean 동일성여부 = 제목.equals(비교할_제목);

		//then
		assertThat(동일성여부).isTrue();
	}
}
