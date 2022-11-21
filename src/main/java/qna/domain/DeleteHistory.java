package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;
    @ManyToOne
    private User deletedBy;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
    }

    public static DeleteHistory of(final Question deletedQuestion) {
        if (Objects.isNull(deletedQuestion)) {
            throw new IllegalArgumentException("Question must not be null!");
        }

        return new DeleteHistory(ContentType.QUESTION, deletedQuestion.getId(), deletedQuestion.getWriter());
    }

    public static DeleteHistory of(final Answer deletedAnswer) {
        if (Objects.isNull(deletedAnswer)) {
            throw new IllegalArgumentException("Answer must not be null!");
        }

        return new DeleteHistory(ContentType.ANSWER, deletedAnswer.getId(), deletedAnswer.getWriter());
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
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy);
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
