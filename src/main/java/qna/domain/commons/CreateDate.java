package qna.domain.commons;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class CreateDate {
  @Column(columnDefinition = "datetime(6)")
  private LocalDateTime createDate = LocalDateTime.now();

  public CreateDate() {}

  public CreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public static CreateDate of(LocalDateTime createDate) {
    return new CreateDate(createDate);
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreateDate that = (CreateDate) o;
    return Objects.equals(createDate, that.createDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createDate);
  }

}
