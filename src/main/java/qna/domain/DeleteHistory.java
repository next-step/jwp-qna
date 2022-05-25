package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteBy;
    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    protected DeleteHistory() {}

    public DeleteHistory(ContentType contentType, Long contentId, User deleteBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteBy = deleteBy;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleteBy, that.deleteBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleteBy.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
