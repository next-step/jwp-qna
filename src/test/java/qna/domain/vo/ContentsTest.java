package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ContentsTest {

	@DisplayName("generate() : 모두 허용")
	@ParameterizedTest(name = "{index} - contents:[{0}], exceptedNotThrownException:{1}")
	@CsvSource(value = {";true", "' ';true", "0;true", "100;true", "가갸거겨고교구규그기;true"}, delimiter = ';')
	void generate(String contents, boolean exceptedNotThrownException) {
		//given

		//when

		//then - not throw any Exception
		assertThatCode(() -> Contents.generate(contents)).doesNotThrowAnyException();
	}
}
