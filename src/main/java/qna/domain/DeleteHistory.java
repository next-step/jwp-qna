package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "delete_by_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedBy) {
        this(contentType, contentId, deletedBy, LocalDateTime.now());
    }

    private DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public DeleteHistory(Answer answer) {
        this(ContentType.ANSWER, answer.getId(), answer.getWriter());
    }

    public DeleteHistory(Question question) {
        this(ContentType.QUESTION, question.getId(), question.getWriter());
    }

    public static DeleteHistory of(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        if (deletedBy == null) {
            throw new IllegalArgumentException("삭제자를 입력하세요");
        }
        return new DeleteHistory(contentType, contentId, deletedBy, createDate);
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
        return contentType == that.contentType && Objects.equals(contentId, that.contentId) && Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId, deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedBy=" + deletedBy +
                ", createDate=" + createDate +
                '}';
    }
}
