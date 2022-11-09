package qna.domain.question.title;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TitleTest {

    @DisplayName("제한 된 길이를 넘어가면 EX 발생")
    @Test
    void length() {
        String success = "일반 제목 길이";
        String ex = "100자가 넘어가는 길이의 제목은 오류가 발생합니다." + "100자가 넘어가는 길이의 제목은 오류가 발생합니다."
                + "100자가 넘어가는 길이의 제목은 오류가 발생합니다." + "100자가 넘어가는 길이의 제목은 오류가 발생합니다.";
        assertThatNoException().isThrownBy(() -> new Title(success));
        assertThatIllegalArgumentException().isThrownBy(() -> new Title(ex));
    }

    @DisplayName("null 이거나 empty 이면 EX 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmpty(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Title(title));
    }
}
