package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PasswordTest {


	@DisplayName("generate() : Null 금지, 20byte 초과 제한")
	@ParameterizedTest(name = "{index} - password:[{0}], exceptedNotThrownException:{1}")
	@CsvSource(value = {";false", "' ';false", "0;true", "100;true"
		, "가갸거겨고교구규그기나냐너녀노뇨누뉴느니;false", "가갸거겨고교구;false", "가갸거겨고교;true"
		, "abcdefghijabcdefghija;false", "abcdefghijabcdefghij;true"}
		, delimiter = ';')
	void generate(String password, boolean exceptedNotThrownException) {
		//given

		//when
		if (exceptedNotThrownException) {
			//then - not throw any Exception
			assertThatCode(() -> Password.generate(password)).doesNotThrowAnyException();
			return;
		}
		//then - throw NumberFormatException
		assertThatThrownBy(() -> Password.generate(password))
			.isInstanceOfAny(IllegalArgumentException.class);
	}
}
