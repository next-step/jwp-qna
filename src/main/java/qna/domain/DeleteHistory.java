package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

@Entity
public class DeleteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column
    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by_id", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User writer;

    @Column
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    public static DeleteHistory ofQuestion(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.QUESTION, contentId, deletedBy);
    }

    public static DeleteHistory ofAnswer(Long contentId, User deletedBy) {
        return new DeleteHistory(ContentType.ANSWER, contentId, deletedBy);
    }

    private DeleteHistory(ContentType contentType, Long contentId, User writer) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.writer = writer;
    }

    public DeleteHistory(ContentType contentType, Long contentId, User writer, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.writer = writer;
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
            && Objects.equals(writer.getId(), that.writer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, writer);
    }

    @Override
    public String toString() {
        return "DeleteHistory{"
            + "id=" + id
            + ", contentType=" + contentType
            + ", contentId=" + contentId
            + ", deletedById=" + writer.getId()
            + ", createDate=" + createDate
            + '}';
    }
}
