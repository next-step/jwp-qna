package qna.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidationUtilTest {

    @ParameterizedTest
    @CsvSource(value = {
            "제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다:100",
            "mwkwonmwkwonmwkwonmwkwon:20"
    }, delimiter =':')
    void 문자열_길이_초과시_에러_발생(String value, int length) {
        assertThatThrownBy(() -> ValidationUtil.checkValidTitleLength(value, length))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void null_이나_비어있는_문자열이_입력_하는_경우_에러_발생(String value) {
        assertThatThrownBy(() -> ValidationUtil.checkValidNullOrEmpty(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
