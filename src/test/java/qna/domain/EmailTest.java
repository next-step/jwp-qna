package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmailTest {
    @Test
    @DisplayName("값이 잘 들어가는지 확인")
    void 데이터_확인() {
        assertThat(Email.of("jennie@hello.com").isEmpty()).isFalse();
    }
}
