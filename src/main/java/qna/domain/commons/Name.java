package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
  @Column(nullable = false, length = 20)
  private String name;

  public Name() {}

  protected Name(String name) {
    this.name = name;
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
