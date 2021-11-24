package qna.domain.commons;

import qna.constants.EntityField;
import qna.utils.ValidationUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
  private static final int NAME_LENGTH = 20;
  private static final EntityField FIELD = EntityField.NAME;

  @Column(nullable = false, length = NAME_LENGTH)
  private String name;

  public Name() {}

  protected Name(String name) {
    checkNameValidation(name);

    this.name = name;
  }

  private void checkNameValidation(String name) {
    ValidationUtils.stringNullOrEmptyCheck(name, "이름은 필수 입력 항목입니다.");
    ValidationUtils.fieldLengthCheck(name, NAME_LENGTH, FIELD);
  }

  public static Name of(String name) {
    return new Name(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Name name1 = (Name) o;
    return Objects.equals(name, name1.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

}
