package qna.domain.wrappers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TitleTest {

    @Test
    void 제목_원시값_포장_객체_생성() {
        Title title = new Title("title");
        assertThat(title).isEqualTo(new Title("title"));
    }

    @Test
    void 제목_길이가_100_초과_하는_경우_에러_발생() {
        String title ="제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다제목입니다";
        assertThatThrownBy(() -> new Title(title))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력 가능한 길이는 100자 이하만 입력 가능합니다.");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void 제목이_null_이나_비어있는_문자열이_입력_하는_경우_에러_발생(String title) {
        assertThatThrownBy(() -> new Title(title))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력값이 입력되지 않았습니다.");
    }
}
