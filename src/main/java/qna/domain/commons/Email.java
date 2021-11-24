package qna.domain.commons;

import qna.constants.EntityField;
import qna.utils.ValidationUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.security.InvalidParameterException;
import java.util.Objects;

@Embeddable
public class Email {
  private static final int EMAIL_LENGTH = 50;
  private static final EntityField FIELD = EntityField.EMAIL;
  private static final String EMAIL_REGEX = "[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+";

  @Column(nullable = false, length = EMAIL_LENGTH)
  private String email;

  public Email() {}

  public Email(String email) {
    checkEmailValidation(email);

    this.email = email;
  }

  private void checkEmailValidation(String email) {
    ValidationUtils.stringNullOrEmptyCheck(email, "이메일은 필수 입력 항목입니다.");

    ValidationUtils.fieldLengthCheck(email, EMAIL_LENGTH, FIELD);

    if (!ValidationUtils.patternMatchCheck(email, EMAIL_REGEX)) {
      throw new InvalidParameterException("이메일 주소 형식이 올바르지 않습니다.");
    }
  }

  public static Email of(String email) {
    return new Email(email);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email1 = (Email) o;
    return Objects.equals(email, email1.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

}
