package qna.domain;

import java.time.*;
import java.util.*;

import javax.persistence.Id;
import javax.persistence.*;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.*;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "delete_history")
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedById;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedById, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createDate = createDate;
    }

    public static DeleteHistory questionDeleteHistoryOf(Long contentId, User deletedById) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedById, LocalDateTime.now());
    }

    public static DeleteHistory answerDeleteHistoryOf(Long contentId, User deletedById) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedById, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id) &&
            contentType == that.contentType &&
            Objects.equals(contentId, that.contentId) &&
            Objects.equals(deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", contentType=" + contentType +
            ", contentId=" + contentId +
            ", deletedById.getId=" + deletedById.getId() +
            ", createDate=" + createDate +
            '}';
    }
}
