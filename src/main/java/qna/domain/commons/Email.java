package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {
  @Column(nullable = false, length = 50)
  private String email;

  public Email() {}

  public Email(String email) {
    this.email = email;
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
