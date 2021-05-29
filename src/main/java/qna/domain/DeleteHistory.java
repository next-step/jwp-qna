package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "deleted_by_id")
    private Long deletedById;

    @Column(name="create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getDeletedById() {
        return deletedById;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getContentId(),
            that.getContentId()) && getContentType() == that.getContentType() && Objects.equals(getDeletedById(),
            that.getDeletedById()) && Objects.equals(getCreateDate(), that.getCreateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContentId(), getContentType(), getDeletedById(), getCreateDate());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentId=" + contentId +
            ", contentType=" + contentType +
            ", deletedById=" + deletedById +
            ", createDate=" + createDate +
            '}';
    }
}
