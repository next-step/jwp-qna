package qna.domain.commons;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PasswordTest {
  @Test
  void 비밀번호_필수_입력_체크() {
    assertThatThrownBy(() -> new Password("")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Password(" ")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Password(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void 이름_최대_길이_체크() {
    assertThatThrownBy(() -> new Password("abcdefghijkalmnopqrstuvwxyz"))
      .isInstanceOf(InvalidParameterException.class);
  }

  @Test
  void 비밀번호_유효성_체크() {
    assertThatThrownBy(() -> new Password("abcdefg")).isInstanceOf(InvalidParameterException.class);
    assertThatThrownBy(() -> new Password("abcdbefg@")).isInstanceOf(InvalidParameterException.class);
    assertThatThrownBy(() -> new Password("Abcdefg!@")).isInstanceOf(InvalidParameterException.class);
  }
}
