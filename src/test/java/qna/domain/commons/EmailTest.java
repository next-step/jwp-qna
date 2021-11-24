package qna.domain.commons;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmailTest {
  @Test
  void 유효하지_않은_이메일_포맷_예외() {
    assertThatThrownBy(() -> new Email("@gmail.com"))
      .isInstanceOf(InvalidParameterException.class);
  }

  @Test
  void 이메일_길이_초과_예외() {
    assertThatThrownBy(() -> new Email("abcdefghigklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz@gmail.com"))
      .isInstanceOf(InvalidParameterException.class);
  }
}
