package qna.domain.commons;

import qna.constants.EntityField;
import qna.utils.ValidationUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.security.InvalidParameterException;

@Embeddable
public class Password {
  private static final int PASSWORD_LENGTH = 20;
  private static final EntityField FIELD = EntityField.PASSWORD;
  private static final String PASSWORD_REGEX = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}";


  @Column(nullable = false, length = PASSWORD_LENGTH)
  private String password;

  protected Password() {}

  public Password(String password) {
    checkPasswordValidation(password);
    this.password = password;
  }

  private void checkPasswordValidation(String password) {
    ValidationUtils.stringNullOrEmptyCheck(password, "비밀번호는 필수 입력 항목입니다.");

    ValidationUtils.fieldLengthCheck(password, PASSWORD_LENGTH, FIELD);

    if (!ValidationUtils.patternMatchCheck(password, PASSWORD_REGEX)) {
      throw new InvalidParameterException("비밀번호 형식이 올바르지 않습니다.");
    }
  }

  public static Password of(String password) {
    return new Password(password);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
