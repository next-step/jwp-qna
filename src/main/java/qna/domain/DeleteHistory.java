package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table(name="delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name="content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User userWhoDeleted;
    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User userWhoDeleted, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.userWhoDeleted = userWhoDeleted;
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
                Objects.equals(userWhoDeleted, that.userWhoDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, userWhoDeleted);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + userWhoDeleted +
                ", createDate=" + createDate +
                '}';
    }
}
