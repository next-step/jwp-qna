package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Deleted {
  @Column(columnDefinition = "bit", nullable = false)
  private boolean deleted = false;

  public Deleted toFalse() {
    deleted = false;
    return this;
  }

  public Deleted toTrue() {
    deleted = true;
    return this;
  }

  public boolean is() {
    return deleted;
  }
}
