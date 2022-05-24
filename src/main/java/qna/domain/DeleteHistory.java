package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this(null, contentType, contentId, deletedBy, createDate);
    }

    private DeleteHistory(Long id, ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.id = id;
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = User.from(deletedBy);
        this.createDate = createDate;
    }

    public static DeleteHistory from(DeleteHistory deleteHistory) {
        if (Objects.isNull(deleteHistory)) {
            return null;
        }
        return new DeleteHistory(
                deleteHistory.id,
                deleteHistory.contentType,
                deleteHistory.contentId,
                deleteHistory.deletedBy,
                deleteHistory.createDate);
    }

    public static DeleteHistory of(ContentType contentType, Long contentId, User writer) {
        return new DeleteHistory(contentType, contentId, writer, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public User deletedBy() {
        return deletedBy;
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentId=" + contentId +
                ", contentType=" + contentType +
                ", createDate=" + createDate +
                ", deletedBy=" + deletedBy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id)
                && Objects.equals(contentId, that.contentId)
                && contentType == that.contentType
                && Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, contentType, createDate, deletedBy);
    }
}
