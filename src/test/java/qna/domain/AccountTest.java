package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountTest {
    @Test
    @DisplayName("동등성 검사 테스트. userId만 같으면 동등")
    void equals() {
        assertThat(new Account("javajigi", "password")).isEqualTo(new Account("javajigi", "1q2w3e4r!"));
    }
}
