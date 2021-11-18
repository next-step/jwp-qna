package qna.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private final ContentType contentType;

    @Embedded
    private final DeleteHistoryContentId contentId;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private final User deletedBy;

    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory(ContentType contentType, Long contentId, User deletedBy) {
        this(contentType, contentId, deletedBy, LocalDateTime.now());
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = new DeleteHistoryContentId(contentId);
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static DeleteHistory ofQuestion(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedBy);
    }

    public static DeleteHistory ofAnswer(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedBy);
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Long getContentId() {
        return contentId.getContentId();
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
