package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "delete_history")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "content_type")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @ManyToOne
    @JoinColumn(name = "delete_by_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(Long contentId, User deletedBy) {
        this.contentId = contentId;
        this.deletedBy = deletedBy;
    }

    public static DeleteHistory of(Long contentId, User deletedBy) {
        if (deletedBy == null) {
            throw new IllegalArgumentException("삭제자를 입력하세요");
        }
        return new DeleteHistory(contentId, deletedBy);
    }

    // TODO 리팩토링에 따라 삭제 예정
    public static DeleteHistory of(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        if (deletedBy == null) {
            throw new IllegalArgumentException("삭제자를 입력하세요");
        }
        return new DeleteHistory(contentId, deletedBy);
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, that.id) && Objects.equals(contentId, that.contentId) && Objects.equals(deletedBy, that.deletedBy) && Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, deletedBy, createDate);
    }
}
