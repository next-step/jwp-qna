package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "deleted_by_id",
        foreignKey = @ForeignKey(name = "fk_delete_history_to_user")
    )
    private User deletedBy;

    protected DeleteHistory() {
    }

    private DeleteHistory(
        final ContentType contentType,
        final Long contentId,
        final User deletedBy,
        final LocalDateTime createDate
    ) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static DeleteHistory ofQuestion(
        final Long contentId,
        final User deletedBy,
        final LocalDateTime createDate
    ) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedBy, createDate);
    }

    public static DeleteHistory ofAnswer(
        final Long contentId,
        final User deletedBy,
        final LocalDateTime createDate
    ) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedBy, createDate);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedById=" + deletedBy.getId() +
            ", createDate=" + createDate +
            '}';
    }
}
