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

    @Column(length = 255)
    private ContentType contentType;

    @Column
    private Long contentId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteBy;

    protected DeleteHistory() { }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteBy) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteBy = deleteBy;
    }

    public Long getId() {
        return id;
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
        return Objects.hash(id, contentType, contentId, deleteBy.getId());
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
