package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserId {
  @Column(nullable = false, length = 20, unique = true)
  private String userId;

  public UserId(String userId) {
    this.userId = userId;
  }

  protected UserId() {}

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
