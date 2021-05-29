package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Table
@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private Long contentId;

    @Column
    private Long deletedById;

    @Column
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public DeleteHistory(Question question, User writer) {
        this(ContentType.QUESTION, question.getId(), writer.getId());
    }

    public DeleteHistory(Answer answer, User writer) {
        this(ContentType.ANSWER, answer.getId(), writer.getId());
    }

    private DeleteHistory(ContentType contentType, Long contentId, Long deletedById) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
    }

    public DeleteHistory(ContentType contentType, Long contentId, Long deletedById, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedById = deletedById;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        DeleteHistory that = (DeleteHistory)object;

        return Objects.equals(id, that.id)
            && contentType == that.contentType
            && Objects.equals(contentId, that.contentId)
            && Objects.equals(deletedById, that.deletedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedById);
    }

    @Override
    public String toString() {
        return "DeleteHistory{"
            + "id=" + id
            + ", contentType=" + contentType
            + ", contentId=" + contentId
            + ", deletedById=" + deletedById
            + ", createDate=" + createDate
            + '}';
    }
}
