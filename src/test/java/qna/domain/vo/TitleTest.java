package qna.domain.vo;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TitleTest {

	@DisplayName("generate() : Null 금지, 100byte 초과 제한")
	@ParameterizedTest(name = "{index} - title:[{0}], exceptedNotThrownException:{1}")
	@CsvSource(value = {";false", "' ';true", "0;true", "100;true"
		, "가갸거겨고교구규그기나냐너녀노뇨누뉴느니다댜더뎌도됴두듀드디라랴러려로료루류르리마먀머며모묘무뮤므미"
		+ "가갸거겨고교구규그기나냐너녀노뇨누뉴느니다댜더뎌도됴두듀드디라랴러려로료루류르리마먀머며모묘무뮤므미;false"
		, "가갸거겨고교구규그기나냐너녀노뇨누뉴느니다댜더뎌도됴두듀드디라랴러려;false"
		, "가갸거겨고교구규그기나냐너녀노뇨누뉴느니다댜더뎌도됴두듀드디라랴러;true"
		, "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"
		+ "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghija;false"
		, "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij"
		+ "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghij;true"}
		, delimiter = ';')
	void generate(String title, boolean exceptedNotThrownException) {
		//given

		//when
		if (exceptedNotThrownException) {
			//then - not throw any Exception
			assertThatCode(() -> Title.generate(title)).doesNotThrowAnyException();
			return;
		}
		//then - throw NumberFormatException
		assertThatThrownBy(() -> Title.generate(title))
			.isInstanceOfAny(IllegalArgumentException.class);
	}
}
