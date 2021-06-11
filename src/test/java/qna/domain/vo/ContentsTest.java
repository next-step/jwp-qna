package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ContentsTest {

	@Test
	void 생성() {
		//given
		String text = "내용";

		//when
		Contents 내용 = Contents.generate(text);

		//then
		assertThat(내용).isNotNull();
	}

	@Test
	void 생성_null() {
		//given
		String null값 = null;

		//when
		Contents 내용 = Contents.generate(null값);

		//then
		assertThat(내용).isNotNull();
	}

	@Test
	void 생성_빈문자열() {
		//given
		String 빈_문자열 = "";

		//when
		Contents 내용 = Contents.generate(빈_문자열);

		//then
		assertThat(내용).isNotNull();
	}

	@Test
	void 변경() {
		//given
		Contents 내용 = Contents.generate("내용");
		String 변경할_내용 = "변경된 내용";

		//when
		내용.changeContents(변경할_내용);

		//then
		assertThat(내용.value()).isEqualTo(변경할_내용);
	}

	@Test
	void 변경_null() {
		//given
		Contents 내용 = Contents.generate("내용");
		String null값 = null;

		//when
		내용.changeContents(null값);

		//then
		assertThat(내용.value()).isEqualTo(null값);
	}

	@Test
	void 변경_빈문자열() {
		//given
		Contents 내용 = Contents.generate("내용");
		String 빈_문자열 = "";

		//when
		내용.changeContents(빈_문자열);

		//then
		assertThat(내용.value()).isEqualTo(빈_문자열);
	}

	@Test
	void 동일성() {
		//given
		Contents 내용 = Contents.generate("내용");
		Contents 비교할내용 = Contents.generate("");
		내용.changeContents(비교할내용.value());

		//when
		boolean 동일성여부 = 내용.equals(비교할내용);

		//then
		assertThat(동일성여부).isTrue();
	}

}
