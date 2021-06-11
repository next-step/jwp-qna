package qna.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
		String text = null;

		//when
		Contents 내용 = Contents.generate(text);

		//then
		assertThat(내용).isNotNull();
	}

	@Test
	void 생성_빈문자열() {
		//given
		String text = "";

		//when
		Contents 내용 = Contents.generate(text);

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
		assertThat(내용).isNotNull();
	}

	@Test
	void 변경_null() {
		//given
		Contents 내용 = Contents.generate("내용");
		String 변경할_내용 = null;

		//when
		내용.changeContents(변경할_내용);

		//then
		assertThat(내용).isNotNull();
	}

	@Test
	void 변경_빈문자열() {
		//given
		Contents 내용 = Contents.generate("내용");
		String 변경할_내용 = "";

		//when
		내용.changeContents(변경할_내용);

		//then
		assertThat(내용).isNotNull();
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
