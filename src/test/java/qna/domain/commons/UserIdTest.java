package qna.domain.commons;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserIdTest {
  @Test
  void 사용자_ID_필수_입력_체크() {
    assertThatThrownBy(() -> new UserId("")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new UserId(" ")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new UserId(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void 사용자_ID_최대_길이_체크() {
    assertThatThrownBy(() -> new UserId("abcdefghijkalmnopqrstuvwxyz"))
      .isInstanceOf(InvalidParameterException.class);
  }
}
