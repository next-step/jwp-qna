package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content_type", length = 255)
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @Column(name = "deleted_by_id")
    private Long deletedById;

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createdAt) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        super.createdAt = createdAt;
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
    }

    protected DeleteHistory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
                ", createDate=" + super.createdAt +
                '}';
    }
}


