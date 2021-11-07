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
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "created_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "deleted_by_id")
    private Long deletedById;

    protected DeleteHistory() {

    }

    public DeleteHistory(Long contentId, ContentType contentType, Long deletedById) {
        this.id = null;
        this.contentId = contentId;
        this.contentType = contentType;
        this.createDate = LocalDateTime.now();
        this.deletedById = deletedById;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedById +
                ", createDate=" + createDate +
                '}';
    }
}
