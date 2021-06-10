package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_histoy")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteUser;

    @Column(name = "deleted_date")
    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory(ContentType contentType, Long contentId, User deleteUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deleteUser;
        this.createDate = createDate;
    }

    protected DeleteHistory() {
    }

    public void setDeleteUser(User deleteUser) {
        this.deleteUser = deleteUser;
    }

    public User getDeleteUser() {
        return deleteUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleteUser.getUserId(), that.deleteUser.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteUser.getUserId());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleteUser.getUserId() +
                ", createDate=" + createDate +
                '}';
    }
}
