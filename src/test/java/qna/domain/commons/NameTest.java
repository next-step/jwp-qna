package qna.domain.commons;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class NameTest {
  @Test
  void 이름_필수_입력_체크() {
    assertThatThrownBy(() -> new Name("")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Name(" ")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Name(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void 이름_최대_길이_체크() {
    assertThatThrownBy(() -> new Name("abcdefghijkalmnopqrstuvwxyz"))
      .isInstanceOf(InvalidParameterException.class);
  }
}
