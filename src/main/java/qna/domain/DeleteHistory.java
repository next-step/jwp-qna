package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static qna.utils.ValidationUtils.isEmpty;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", updatable = false, foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    @CreatedDate
    private LocalDateTime createDate;

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        validation(deletedByUser);
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
    }

    protected DeleteHistory() {
    }

    private void validation(User deletedByUser) {
        if (isEmpty(deletedByUser)) {
            throw new UnAuthorizedException("삭제한 사용자가 없습니다.");
        }
    }

    public User getDeletedByUser() {
        return deletedByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id)
                && contentType == that.contentType
                && Objects.equals(contentId, that.contentId)
                && Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }
}
