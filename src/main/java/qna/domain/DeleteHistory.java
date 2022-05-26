package qna.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    private User deleteUser;

    protected DeleteHistory() {
    }

    protected DeleteHistory(ContentType contentType, Long contentId, User deleteUser) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleteUser = deleteUser;
    }

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWrittenBy());
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWrittenBy());
    }

    public Long getId() {
        return id;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public User getDeleteUser() {
        return deleteUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) && Objects.equals(contentId, that.contentId) && contentType == that.contentType && Objects.equals(createDate, that.createDate) && Objects.equals(deleteUser, that.deleteUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentId, contentType, createDate, deleteUser);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" + "id=" + id + ", contentType=" + contentType + ", contentId=" + contentId + ", user=" + deleteUser + ", createDate=" + createDate + '}';
    }
}
