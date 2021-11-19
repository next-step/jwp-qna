package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.Name.MAX_NAME_LENGTH;
import static qna.domain.UserId.MAX_USER_ID_LENGTH;
import static qna.util.TestUtils.createStringLongerThan;

class NameTest {

    @Test
    @DisplayName("이름 정상 생성")
    void name_생성() {
        // given
        String name = "박성민";

        // when
        Name result = new Name(name);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("이름에 null이 입력될 경우 실패한다.")
    void name_null() {
        // when, then
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 반드시 입력되어야 합니다. (최대 길이: " + MAX_NAME_LENGTH + ")");
    }

    @Test
    @DisplayName("이름의 길이가 최대 길이보다 긴 경우 생성 실패한다.")
    void name_길이_초과() {
        // given
        String name = createStringLongerThan(MAX_NAME_LENGTH);

        // when, then
        assertThatThrownBy(() -> new Name(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 " + MAX_NAME_LENGTH + "자 이하로 입력 가능합니다. (입력값: " + MAX_NAME_LENGTH + ")");
    }
}