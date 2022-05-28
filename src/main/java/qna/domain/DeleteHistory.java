package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

@Entity
@Table(name = "DELETE_HISTORY")
public class DeleteHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "CONTENT_TYPE")
    private ContentType contentType;
    @Column(name = "CONTENT_ID")
    private Long contentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELETED_BY_ID", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getContentId() {
        return contentId;
    }

    public User getDeletedByUser() {
        return deletedByUser;
    }


    public boolean isDeletedBy(Predicate<DeleteHistory> predicate) {
        if (Objects.isNull(predicate)) {
            throw new IllegalArgumentException("invalid parameter");
        }
        return predicate.test(this);
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
                ", deletedByUser=" + deletedByUser +
                ", createDate=" + createDate +
                '}';
    }
}
