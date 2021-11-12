package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory extends GeneratedId {
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delete_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByWriter;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByWriter) {
        this(null, contentType, contentId, deletedByWriter);
    }

    public DeleteHistory(Long id, ContentType contentType, Long contentId, User deletedByWriter) {
        super.setId(id);
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByWriter = deletedByWriter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(getId(), that.getId())
                && contentType == that.contentType
                && Objects.equals(contentId, that.contentId)
                && Objects.equals(deletedByWriter, that.deletedByWriter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), contentType, contentId, deletedByWriter);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + getId() +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedByWriter=" + deletedByWriter +
                ", createdDate=" + createdDate +
                '}';
    }
}
