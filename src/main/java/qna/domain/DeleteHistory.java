package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;
    @Column(name = "create_date")
    protected LocalDateTime createDate;

    protected DeleteHistory() {}

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(this.id, that.id) &&
                this.contentType == that.contentType &&
                Objects.equals(this.contentId, that.contentId) &&
                Objects.equals(this.deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.contentType, this.contentId, this.deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + this.id +
                ", contentType=" + this.contentType +
                ", contentId=" + this.contentId +
                ", deletedBy=" + this.deletedBy +
                ", createDate=" + this.createDate +
                '}';
    }
}
