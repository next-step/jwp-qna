package qna.domain.commons;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TitleTest {
  @Test
  void 제목_필수_입력_체크() {
    assertThatThrownBy(() -> new Title("")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Title(" ")).isInstanceOf(NullPointerException.class);
    assertThatThrownBy(() -> new Title(null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void 제목_최대_길이_체크() {
    assertThatThrownBy(() -> new Title(
      "abcdefghijkalmnopqrstuvwxyz" +
      "abcdefghijkalmnopqrstuvwxyz" +
      "abcdefghijkalmnopqrstuvwxyz" +
      "abcdefghijkalmnopqrstuvwxyz" +
      "abcdefghijkalmnopqrstuvwxyz"))
      .isInstanceOf(InvalidParameterException.class);
  }
}
