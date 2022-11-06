package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "deleteHistory")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    @Column(name = "content_type", length = 255)
    private ContentType contentType;
    @Column(name = "content_id")
    private Long contentId;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @JoinColumn(name = "deleted_by_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    @ManyToOne
    private User deleteByUser;


    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteByUser = deleteByUser;
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
                Objects.equals(deleteByUser, that.deleteByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleteByUser.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
