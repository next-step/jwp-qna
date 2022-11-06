package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @ManyToOne
    @JoinColumn(name = "deleted_by_id", nullable = false, foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deleter;

    private LocalDateTime createDate = LocalDateTime.now();

    public DeleteHistory(ContentType contentType, Long contentId, User deleter, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deleter = deleter;
        this.createDate = createDate;
    }

    protected DeleteHistory() {

    }

    public static DeleteHistory ofAnswer(Long contentId, User deleter) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deleter, LocalDateTime.now());
    }

    public static DeleteHistory ofQuestion(Long contentId, User deleter) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deleter, LocalDateTime.now());
    }

    public Long getId() {
        return this.id;
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
                Objects.equals(deleter, that.deleter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deleter);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", deletedById=" + deleter.getId() +
                ", createDate=" + createDate +
                '}';
    }
}
