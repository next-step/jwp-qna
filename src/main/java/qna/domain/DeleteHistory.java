package qna.domain;

import javassist.expr.Instanceof;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {

    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getContentId() {
        return contentId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof DeleteHistory)) return false;
        DeleteHistory deleteHistory = (DeleteHistory) o;
        if (id != null && Objects.equals(id, deleteHistory.getId())) return true;
        return Objects.equals(id, deleteHistory.getId()) && contentType == deleteHistory.getContentType() && Objects.equals(contentId, deleteHistory.getContentId()) && Objects.equals(deletedBy, deleteHistory.getDeletedBy()) && Objects.equals(createDate.getSecond(), deleteHistory.getCreateDate().getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy, createDate);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", createDate=" + createDate +
                '}';
    }
}
