package qna.domain.commons;

import qna.constants.EntityField;
import qna.utils.ValidationUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
  private static final int USER_ID_LENGTH = 20;
  private static final EntityField FIELD = EntityField.USER_ID;
  @Column(nullable = false, length = USER_ID_LENGTH, unique = true)
  private String userId;

  protected UserId() {}
  
  public UserId(String userId) {
    checkUserIdValidation(userId);
    
    this.userId = userId;
  }

  private void checkUserIdValidation(String userId) {
    ValidationUtils.stringNullOrEmptyCheck(userId, "사용자 ID는 필수 입력 항목입니다.");
    ValidationUtils.fieldLengthCheck(userId, USER_ID_LENGTH, FIELD);
  }

  public static UserId of(String userId) {
    return new UserId(userId);
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserId userId1 = (UserId) o;
    return Objects.equals(userId, userId1.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

}
