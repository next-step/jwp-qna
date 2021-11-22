package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {
  @Column(nullable = false, length = 20)
  private String password;

  protected Password() {}

  public Password(String password) {
    this.password = password;
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
