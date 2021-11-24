package qna.domain.commons;

import qna.constants.EntityField;
import qna.utils.ValidationUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {
  private static final int TITLE_LENGTH = 100;
  private static final EntityField FIELD = EntityField.TITLE;
  @Column(length = TITLE_LENGTH, nullable = false)
  private String title;

  protected Title() {}

  public Title(String title) {
    checkTitleValidation(title);

    this.title = title;
  }

  private void checkTitleValidation(String title) {
    ValidationUtils.stringNullOrEmptyCheck(title, "제목은 필수 입력 항목입니다.");
    ValidationUtils.fieldLengthCheck(title, TITLE_LENGTH, FIELD);
  }

  public static Title of(String title) {
    return new Title(title);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}