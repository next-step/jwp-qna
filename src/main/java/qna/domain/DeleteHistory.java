package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @Column(name = "deleted_by_id")
    private Long deletedById;

    protected DeleteHistory() {}

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createdAt) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(this.id, that.id) &&
                this.contentType == that.contentType &&
                Objects.equals(this.contentId, that.contentId) &&
                Objects.equals(this.deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.contentType, this.contentId, this.deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + this.id +
                ", contentType=" + this.contentType +
                ", contentId=" + this.contentId +
                ", deletedById=" + this.deletedById +
                ", createdAt=" + this.createdAt +
                '}';
    }
}
