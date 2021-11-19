package qna.domain.deletehistory;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import qna.domain.qna.Answer;
import qna.domain.qna.Question;
import qna.domain.user.User;

@Entity
@Table(name = "delete_history")
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    @Embedded
    private DeleteTarget deleteTarget;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    private DeleteHistory(DeleteTarget deleteTarget, User deletedBy,
        LocalDateTime createDate) {
        this.deleteTarget = deleteTarget;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static DeleteHistory OfQuestion(Question question, User deletedBy) {
        DeleteTarget deleteTarget = DeleteTarget.of(question.getId(),
            ContentType.QUESTION);

        return new DeleteHistory(deleteTarget, deletedBy, LocalDateTime.now());
    }

    public static DeleteHistory OfAnswer(Answer answer, User deletedBy) {
        DeleteTarget deleteTarget = DeleteTarget.of(answer.getId(),
            ContentType.ANSWER);

        return new DeleteHistory(deleteTarget, deletedBy, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return deleteTarget.getContentType();
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
            "id=" + id +
            ", deleteTarget=" + deleteTarget +
            ", deletedBy=" + deletedBy.getId() +
            ", createDate=" + createDate +
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
        return Objects.equals(id, that.id) && Objects.equals(deleteTarget,
            that.deleteTarget) && Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleteTarget, deletedBy);
    }
}
