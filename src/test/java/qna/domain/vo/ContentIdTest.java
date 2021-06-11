package qna.domain.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ContentIdTest {

	@Test
	void 생성() {
		//given
		Long id = 1L;

		//when
		ContentId 컨텐츠아이디 = ContentId.generate(id);

		//then
		assertThat(컨텐츠아이디).isNotNull();
	}

	@Test
	void 생성_최대값() {
		//given
		Long id = Long.MAX_VALUE;

		//when
		ContentId 컨텐츠아이디 = ContentId.generate(id);

		//then
		assertThat(컨텐츠아이디).isNotNull();
	}

	@Test
	void 생성_최소값() {
		//given
		Long id = Long.MIN_VALUE;

		//when
		ContentId 컨텐츠아이디 = ContentId.generate(id);

		//then
		assertThat(컨텐츠아이디).isNotNull();
	}

	@Test
	void 생성_null_예외발생() {
		//given
		Long 컨텐츠아이디 = null;

		//when

		//then
		assertThatThrownBy(() -> ContentId.generate(컨텐츠아이디)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 동일성() {
		//given
		Long id = 1L;
		ContentId 컨텐츠아이디 = ContentId.generate(id);
		ContentId 비교할_컨텐츠아이디 = ContentId.generate(id);

		//when
		boolean 동일성비교 = 비교할_컨텐츠아이디.equals(컨텐츠아이디);

		//then
		assertThat(동일성비교).isTrue();
	}
}
