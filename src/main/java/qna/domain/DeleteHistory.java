package qna.domain;

import qna.domain.commons.ContentType;
import qna.domain.commons.CreateDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private ContentType contentType;

  private Long contentId;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
  private User deletedBy;

  @Embedded
  private CreateDate createDate = new CreateDate();

  protected DeleteHistory() {}

  public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, CreateDate createDate) {
    this.contentType = contentType;
    this.contentId = contentId;
    this.deletedBy = deletedBy;
    this.createDate = createDate;
  }

  public static DeleteHistory ofAnswer(Long id, User deletedBy) {
    return new DeleteHistory(ContentType.ANSWER, id, deletedBy, CreateDate.of(LocalDateTime.now()));
  }

  public static DeleteHistory ofQuestion(Long id, User deletedBy) {
    return new DeleteHistory(ContentType.QUESTION, id, deletedBy, CreateDate.of(LocalDateTime.now()));
  }

  public void toDeletedBy(User deletedBy) {
    this.deletedBy = deletedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DeleteHistory that = (DeleteHistory) o;
    return Objects.equals(id, that.id) &&
      contentType == that.contentType &&
      Objects.equals(contentId, that.contentId) &&
      Objects.equals(deletedBy, that.deletedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contentType, contentId, deletedBy);
  }

  @Override
  public String toString() {
    return "DeleteHistory{" +
      "id=" + id +
      ", contentType=" + contentType +
      ", contentId=" + contentId +
      ", deletedByUser=" + deletedBy +
      ", createDate=" + createDate +
      '}';
  }
}
