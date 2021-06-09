package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ContentIdTest {

	@DisplayName("generate() : null 금지")
	@ParameterizedTest(name = "{index} - contentId:[{0}], exceptedNotThrownException:{1}")
	@CsvSource(value = {";false", "0;true", "100;true"}, delimiter = ';')
	void generate(Long contentId, boolean exceptedNotThrownException) {
		//given

		//when
		if (exceptedNotThrownException) {
			//then - not throw any Exception
			assertThatCode(() -> ContentId.generate(contentId)).doesNotThrowAnyException();
			return;
		}
		//then - throw NumberFormatException
		assertThatThrownBy(() -> ContentId.generate(contentId))
			.isInstanceOfAny(IllegalArgumentException.class);
	}
}
