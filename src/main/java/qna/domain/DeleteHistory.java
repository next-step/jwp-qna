package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;
    private Long deletedById;
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    protected DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriterId());
    }

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(ContentType.ANSWER, question.getId(), question.getWriterId());
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
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
               ", deletedById=" + deletedById +
               ", createDate=" + createDate +
               '}';
    }
}
