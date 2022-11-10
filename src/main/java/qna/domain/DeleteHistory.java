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

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;

    private DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
    }

    protected DeleteHistory() {

    }

    public Long getId() {
        return id;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteHistory)) {
            return false;
        }
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(getId(), that.getId())
                && contentType == that.contentType
                && Objects.equals(contentId, that.contentId)
                && Objects.equals(getDeletedBy(), that.getDeletedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), contentType, contentId, createDate, getDeletedBy());
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedBy=" + deletedBy.toString() +
                ", createDate=" + createDate +
                '}';
    }


}
