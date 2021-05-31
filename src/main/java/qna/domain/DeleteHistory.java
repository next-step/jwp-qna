package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "delete_history")
@Entity
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
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
        // empty
    }


    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id)
               && contentType == that.contentType
               && Objects.equals(contentId, that.contentId)
               && Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DeleteHistory.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("contentType=" + contentType)
            .add("contentId=" + contentId)
            .add("deletedByUser=" + deletedByUser)
            .add("createDate=" + createDate)
            .toString();
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }


    public boolean isSameContent(ContentType contentType) {
        return this.contentType == contentType;
    }

    public boolean isSameOwner(User deletedByUser) {
        return this.deletedByUser == deletedByUser;
    }

    public boolean isSameContentId(long contentId) {
        return this.contentId == contentId;
    }

    public Long getId() {
        return this.id;
    }

    public void toDeletedByUser(final User deletedByUser) {
        this.deletedByUser = deletedByUser;
    }
}
