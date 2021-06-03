package qna.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class DeleteHistory implements Serializable {

    private static final long serialVersionUID = 496753074138618381L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    private ContentType contentType;

    @Column
    private Long contentId;

    @OneToOne
    @JoinColumn(name = "deleted_by_id")
    private User deletedBy;

    private LocalDateTime createDate;

    public DeleteHistory() { }

    public DeleteHistory(ContentType contentType, Long contentId, User deletedBy, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createDate = createDate;
    }

    public static DeleteHistory ofQuestion(Long contentId, User writer, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.QUESTION, contentId, writer, createDate);
    }

    public static DeleteHistory ofAnswer(Long contentId, User writer, LocalDateTime createDate) {
        return new DeleteHistory(ContentType.ANSWER, contentId, writer, createDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
            ", deletedBy=" + deletedBy +
            ", createDate=" + createDate +
            '}';
    }
}
