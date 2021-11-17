package qna.domain.deletehistory;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.UnAuthorizedException;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser) {
        validate(contentType, contentId, deletedByUser);
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
    }

    private void validate(ContentType contentType, Long contentId, User deletedByUser) {
        if (Objects.isNull(deletedByUser)) {
            throw new UnAuthorizedException();
        }

        if (contentId == 0) {
            throw new IllegalArgumentException("삭제한 컨텐츠 아이디가 있어야 합니다.");
        }

        if (contentType == null) {
            throw new IllegalArgumentException("컨텐츠 타입이 존재해야 합니다.");
        }
    }

    public static DeleteHistory ofQuestion(Long contentId, User writer) {
        return new DeleteHistory(ContentType.QUESTION, contentId, writer);
    }

    public static DeleteHistory ofAnswer(Long contentId, User writer) {
        return new DeleteHistory(ContentType.ANSWER, contentId, writer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deletedByUser +
                ", createDate=" + createDate +
                '}';
    }

    public boolean matchContentId(Long id) {
        return this.contentId.compareTo(id) == 0;
    }

    public boolean matchContentType(ContentType contentType) {
        return this.contentType == contentType;
    }

    public boolean matchDeletedUser(User deletedByUser) {
        return this.deletedByUser.equals(deletedByUser);
    }
}
