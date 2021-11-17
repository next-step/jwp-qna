package qna.domain;

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

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;
    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    protected DeleteHistory() {

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
                ", deletedById=" + deletedBy +
                ", createDate=" + createDate +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public Long getContentId() {
        return this.contentId;
    }

    public void setDeletedBy(User deletedBy) {
        this.deletedBy = deletedBy;
    }
}
