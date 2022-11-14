package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contentId;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private LocalDateTime createDate = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleteUser;

    protected DeleteHistory() {

    }

    public DeleteHistory(ContentType contentType, Long contentId, User deleteUser) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deleteUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deleteUser, that.deleteUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleteUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", contentType=" + contentType +
                ", createDate=" + createDate +
                ", deletedById=" + deleteUser.getId() +
                '}';
    }
}
